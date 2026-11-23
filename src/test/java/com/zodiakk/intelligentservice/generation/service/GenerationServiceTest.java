package com.zodiakk.intelligentservice.generation.service;

import com.zodiakk.intelligentservice.career.dto.request.AssessmentScoresDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerExplanationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathCandidateDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathInterpretationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerTargetDto;
import com.zodiakk.intelligentservice.common.enums.AiGenerationStatus;
import com.zodiakk.intelligentservice.common.exception.ResourceNotFoundException;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentAreaContextDto;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentRecommendationRequestDto;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;
import com.zodiakk.intelligentservice.generation.fallback.RussianTemplateGenerator;
import com.zodiakk.intelligentservice.generation.llm.TextGenerationClient;
import com.zodiakk.intelligentservice.generation.repository.AiGenerationRepository;
import com.zodiakk.intelligentservice.model.entity.AiModelConfig;
import com.zodiakk.intelligentservice.model.repository.AiModelConfigRepository;
import com.zodiakk.intelligentservice.prompt.entity.PromptTemplate;
import com.zodiakk.intelligentservice.prompt.repository.PromptTemplateRepository;
import com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerationServiceTest {

    @Mock
    private AiGenerationRepository generationRepository;

    @Mock
    private AiModelConfigRepository modelConfigRepository;

    @Mock
    private PromptTemplateRepository promptTemplateRepository;

    @Mock
    private TextGenerationClient textGenerationClient;

    private GenerationService service;

    @BeforeEach
    void setUp() {
        JsonMapper jsonMapper = new JsonMapper();
        service = new GenerationService(
                generationRepository,
                modelConfigRepository,
                promptTemplateRepository,
                textGenerationClient,
                new RussianTemplateGenerator(),
                new GenerationOutputParser(jsonMapper),
                jsonMapper,
                new ConcurrentMapCacheManager("generations")
        );
    }

    @Test
    void usesTheModelWhenTheJsonIsValid() {
        when(generationRepository.findFirstByTaskTypeAndInputHashAndStatusOrderByCreatedAtDesc(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(modelConfigRepository.findAllByActiveTrue()).thenReturn(List.of(model()));
        when(promptTemplateRepository.findFirstByTaskTypeAndActiveTrueOrderByVersionDesc(any())).thenReturn(Optional.of(prompt()));
        when(textGenerationClient.generate(anyString(), anyString())).thenReturn("""
                {"summary":"Модель объяснила оценку","strengths":["Java"],"developmentAreas":[],"limitations":["Не решение"]}
                """);
        when(generationRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            AiGeneration generation = invocation.getArgument(0);
            generation.setId(UUID.randomUUID());
            return generation;
        });
        when(generationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = service.explain(request());

        assertEquals("qwen2.5:3b", response.getModelIdentifier());
        assertEquals(1, response.getPromptVersion());
        verify(generationRepository).save(any());
    }

    @Test
    void returnsTheStoredGenerationForTheSameInput() {
        AiGeneration stored = AiGeneration.builder()
                .id(UUID.randomUUID())
                .output("{\"summary\":\"Сохранено\",\"strengths\":[],\"developmentAreas\":[],\"limitations\":[],\"modelIdentifier\":\"qwen2.5:3b\"}")
                .status(AiGenerationStatus.COMPLETED)
                .build();
        when(generationRepository.findFirstByTaskTypeAndInputHashAndStatusOrderByCreatedAtDesc(any(), any(), any()))
                .thenReturn(Optional.of(stored));

        var first = service.explain(request());
        var second = service.explain(request());

        assertEquals("Сохранено", first.getSummary());
        assertEquals("Сохранено", second.getSummary());
    }

    @Test
    void rejectsAnUnknownGeneration() {
        when(generationRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.get(UUID.randomUUID()));
    }

    @Test
    void fallsBackWhenTheModelFailsAndReusesTheCache() {
        stubModelAndPrompt();
        when(textGenerationClient.generate(anyString(), anyString())).thenThrow(new IllegalStateException("down"));
        when(generationRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            AiGeneration generation = invocation.getArgument(0);
            generation.setId(UUID.randomUUID());
            return generation;
        });
        when(generationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var first = service.explain(request());
        var second = service.explain(request());

        assertEquals("template-fallback", first.getModelIdentifier());
        assertEquals(AiGenerationStatus.FALLBACK.name(), first.getStatus());
        assertEquals(first.getSummary(), second.getSummary());
        verify(textGenerationClient).generate(anyString(), anyString());
    }

    @Test
    void recommendsInterpretsAndExplainsScreeningFromValidJson() {
        stubModelAndPrompt();
        when(generationRepository.saveAndFlush(any())).thenAnswer(invocation -> {
            AiGeneration generation = invocation.getArgument(0);
            generation.setId(UUID.randomUUID());
            return generation;
        });
        when(generationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(textGenerationClient.generate(anyString(), anyString())).thenReturn("""
                {"recommendations":[{"title":"Kafka","description":"Практика","type":"PROJECT_EXPERIENCE"}]}
                """, """
                {"summary":"Сравнение","directions":[{"targetPositionName":"Senior","fitSummary":"ok"}],"recommendedDirection":"Senior"}
                """, """
                {"summary":"Рейтинг кандидатов","highlights":["балл"],"limitations":["не решение"]}
                """);

        var recommended = service.recommend(DevelopmentRecommendationRequestDto.builder()
                .assessmentId(UUID.randomUUID())
                .language("ru")
                .developmentAreas(List.of(DevelopmentAreaContextDto.builder()
                        .skillId(UUID.randomUUID())
                        .skillName("Kafka")
                        .currentLevel(1)
                        .targetLevel(3)
                        .priorityScore(new BigDecimal("0.10"))
                        .build()))
                .build());
        assertEquals(1, recommended.getRecommendations().size());
        assertNotNull(recommended.getGenerationId());

        var interpreted = service.interpret(CareerPathInterpretationRequestDto.builder()
                .employeeId(UUID.randomUUID())
                .language("ru")
                .directions(List.of(CareerPathCandidateDto.builder()
                        .targetPositionId(UUID.randomUUID())
                        .targetPositionName("Senior")
                        .totalScore(new BigDecimal("0.70"))
                        .mandatoryRequirementsMet(true)
                        .strengths(List.of())
                        .skillGaps(List.of())
                        .build()))
                .build());
        assertEquals("Сравнение", interpreted.getSummary());

        TalentScreeningExplanationRequestDto screening = new TalentScreeningExplanationRequestDto();
        screening.setTargetPositionName("Lead");
        var explained = service.explainScreening(screening);
        assertTrue(explained.getSummary().contains("Рейтинг"));
    }

    @Test
    void returnsStoredGenerationAndFailsWithoutAnActiveModel() {
        UUID id = UUID.randomUUID();
        when(generationRepository.findById(id)).thenReturn(Optional.of(AiGeneration.builder().id(id).build()));
        assertEquals(id, service.get(id).getId());

        when(generationRepository.findFirstByTaskTypeAndInputHashAndStatusOrderByCreatedAtDesc(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(modelConfigRepository.findAllByActiveTrue()).thenReturn(List.of());
        assertThrows(IllegalStateException.class, () -> service.explain(request()));
        verify(textGenerationClient, never()).generate(anyString(), anyString());
    }

    @Test
    void restoresInvalidStoredOutputFallsBackAndFailsWhenTemplatesThrow() {
        when(generationRepository.findFirstByTaskTypeAndInputHashAndStatusOrderByCreatedAtDesc(any(), any(), any()))
                .thenReturn(Optional.of(AiGeneration.builder().output("{").status(AiGenerationStatus.COMPLETED).build()));
        when(modelConfigRepository.findAllByActiveTrue()).thenReturn(List.of(model()));
        when(promptTemplateRepository.findFirstByTaskTypeAndActiveTrueOrderByVersionDesc(any())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> service.explain(request()));

        JsonMapper jsonMapper = new JsonMapper();
        RussianTemplateGenerator templates = org.mockito.Mockito.mock(RussianTemplateGenerator.class);
        GenerationService failing = new GenerationService(
                generationRepository,
                modelConfigRepository,
                promptTemplateRepository,
                textGenerationClient,
                templates,
                new GenerationOutputParser(jsonMapper),
                jsonMapper,
                new org.springframework.cache.concurrent.ConcurrentMapCacheManager("other")
        );
        when(promptTemplateRepository.findFirstByTaskTypeAndActiveTrueOrderByVersionDesc(any())).thenReturn(Optional.of(prompt()));
        when(textGenerationClient.generate(anyString(), anyString())).thenReturn("not-json");
        when(templates.explain(any())).thenThrow(new IllegalStateException("template down"));
        when(generationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CareerExplanationRequestDto withoutCorrelation = request();
        withoutCorrelation.setCorrelationId(null);
        assertThrows(IllegalStateException.class, () -> failing.explain(withoutCorrelation));
    }

    private void stubModelAndPrompt() {
        when(generationRepository.findFirstByTaskTypeAndInputHashAndStatusOrderByCreatedAtDesc(any(), any(), any()))
                .thenReturn(Optional.empty());
        when(modelConfigRepository.findAllByActiveTrue()).thenReturn(List.of(model()));
        when(promptTemplateRepository.findFirstByTaskTypeAndActiveTrueOrderByVersionDesc(any())).thenReturn(Optional.of(prompt()));
    }

    private CareerExplanationRequestDto request() {
        return CareerExplanationRequestDto.builder()
                .correlationId(UUID.fromString("aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa"))
                .assessmentId(UUID.fromString("bbbbbbbb-bbbb-4bbb-8bbb-bbbbbbbbbbbb"))
                .language("ru")
                .dataQualityStatus("COMPLETE")
                .mandatoryRequirementsMet(true)
                .target(CareerTargetDto.builder().positionName("Senior").gradeName("Senior").build())
                .scores(AssessmentScoresDto.builder().skill(BigDecimal.ONE).experience(BigDecimal.ONE).total(BigDecimal.ONE).build())
                .strengths(List.of())
                .skillGaps(List.of())
                .build();
    }

    private AiModelConfig model() {
        return AiModelConfig.builder().id(UUID.randomUUID()).modelName("qwen2.5:3b").active(true).build();
    }

    private PromptTemplate prompt() {
        return PromptTemplate.builder()
                .id(UUID.randomUUID())
                .version(1)
                .systemPrompt("system")
                .userPromptTemplate("user {{input}}")
                .active(true)
                .build();
    }
}
