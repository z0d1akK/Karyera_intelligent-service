package com.zodiakk.intelligentservice.generation.service;

import com.zodiakk.intelligentservice.career.dto.request.AssessmentScoresDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerExplanationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerTargetDto;
import com.zodiakk.intelligentservice.common.enums.AiGenerationStatus;
import com.zodiakk.intelligentservice.common.exception.ResourceNotFoundException;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;
import com.zodiakk.intelligentservice.generation.fallback.RussianTemplateGenerator;
import com.zodiakk.intelligentservice.generation.llm.TextGenerationClient;
import com.zodiakk.intelligentservice.generation.repository.AiGenerationRepository;
import com.zodiakk.intelligentservice.model.entity.AiModelConfig;
import com.zodiakk.intelligentservice.model.repository.AiModelConfigRepository;
import com.zodiakk.intelligentservice.prompt.entity.PromptTemplate;
import com.zodiakk.intelligentservice.prompt.repository.PromptTemplateRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

        assertEquals("qwen2.5:14b", response.getModelIdentifier());
        assertEquals(1, response.getPromptVersion());
        verify(generationRepository).save(any());
    }

    @Test
    void returnsTheStoredGenerationForTheSameInput() {
        AiGeneration stored = AiGeneration.builder()
                .id(UUID.randomUUID())
                .output("{\"summary\":\"Сохранено\",\"strengths\":[],\"developmentAreas\":[],\"limitations\":[],\"modelIdentifier\":\"qwen2.5:14b\"}")
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
        return AiModelConfig.builder().id(UUID.randomUUID()).modelName("qwen2.5:14b").active(true).build();
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
