package com.zodiakk.intelligentservice.generation.service;

import com.zodiakk.intelligentservice.career.dto.request.CareerExplanationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathInterpretationRequestDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerExplanationResponseDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathInterpretationResponseDto;
import com.zodiakk.intelligentservice.common.enums.AiGenerationStatus;
import com.zodiakk.intelligentservice.common.exception.ResourceNotFoundException;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentRecommendationRequestDto;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationResponseDto;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;
import com.zodiakk.intelligentservice.generation.fallback.RussianTemplateGenerator;
import com.zodiakk.intelligentservice.generation.llm.TextGenerationClient;
import com.zodiakk.intelligentservice.generation.repository.AiGenerationRepository;
import com.zodiakk.intelligentservice.model.entity.AiModelConfig;
import com.zodiakk.intelligentservice.model.repository.AiModelConfigRepository;
import com.zodiakk.intelligentservice.prompt.entity.PromptTemplate;
import com.zodiakk.intelligentservice.prompt.repository.PromptTemplateRepository;
import com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationRequestDto;
import com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HexFormat;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerationService {

    public static final String CAREER_EXPLANATION = "CAREER_EXPLANATION";

    public static final String DEVELOPMENT_RECOMMENDATIONS = "DEVELOPMENT_RECOMMENDATIONS";

    public static final String CAREER_PATH_INTERPRETATION = "CAREER_PATH_INTERPRETATION";

    public static final String TALENT_SCREENING_EXPLANATION = "TALENT_SCREENING_EXPLANATION";

    private final AiGenerationRepository generationRepository;

    private final AiModelConfigRepository modelConfigRepository;

    private final PromptTemplateRepository promptTemplateRepository;

    private final TextGenerationClient textGenerationClient;

    private final RussianTemplateGenerator templates;

    private final GenerationOutputParser outputParser;

    private final ObjectMapper objectMapper;

    private final CacheManager cacheManager;

    public CareerExplanationResponseDto explain(CareerExplanationRequestDto request) {
        return generate(CAREER_EXPLANATION, request.getAssessmentId(), request.getCorrelationId(), request,
                CareerExplanationResponseDto.class, () -> templates.explain(request));
    }

    public DevelopmentRecommendationResponseDto recommend(DevelopmentRecommendationRequestDto request) {
        return generate(DEVELOPMENT_RECOMMENDATIONS, request.getAssessmentId(), request.getCorrelationId(), request,
                DevelopmentRecommendationResponseDto.class, () -> templates.recommend(request));
    }

    public CareerPathInterpretationResponseDto interpret(CareerPathInterpretationRequestDto request) {
        return generate(CAREER_PATH_INTERPRETATION, null, request.getCorrelationId(), request,
                CareerPathInterpretationResponseDto.class, () -> templates.interpret(request));
    }

    public TalentScreeningExplanationResponseDto explainScreening(TalentScreeningExplanationRequestDto request) {
        return generate(
                TALENT_SCREENING_EXPLANATION,
                null,
                request.getCorrelationId(),
                request,
                TalentScreeningExplanationResponseDto.class,
                () -> templates.explainScreening(request)
        );
    }

    @Transactional(readOnly = true)
    public AiGeneration get(UUID id) {
        return generationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Генерация не найдена"));
    }

    private <T> T generate(
            String taskType,
            UUID assessmentId,
            UUID correlationId,
            Object request,
            Class<T> type,
            java.util.function.Supplier<T> fallback
    ) {
        String hash = sha256(taskType + ":" + write(request));
        T cached = readCache(taskType, hash, type);
        if (valid(cached, request)) {
            return cached;
        }

        AiGeneration existing = generationRepository
                .findFirstByTaskTypeAndInputHashAndStatusOrderByCreatedAtDesc(taskType, hash, AiGenerationStatus.COMPLETED)
                .orElse(null);
        if (existing != null && existing.getOutput() != null) {
            T restored = read(existing.getOutput(), type);
            if (valid(restored, request)) {
                writeCache(taskType, hash, existing.getOutput());
                return restored;
            }
        }

        AiModelConfig model = modelConfigRepository.findAllByActiveTrue().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Активная модель не настроена"));
        PromptTemplate prompt = promptTemplateRepository.findFirstByTaskTypeAndActiveTrueOrderByVersionDesc(taskType)
                .orElseThrow(() -> new IllegalStateException("Активный промпт не найден"));

        long started = System.currentTimeMillis();
        T result = null;
        String modelIdentifier = model.getModelName();
        AiGenerationStatus status = AiGenerationStatus.COMPLETED;
        String raw = null;
        try {
            raw = textGenerationClient.generate(
                    prompt.getSystemPrompt(),
                    prompt.getUserPromptTemplate().replace("{{input}}", write(request))
            );
            result = outputParser.parse(taskType, raw, type);
        } catch (RuntimeException ex) {
            log.warn("LLM generation failed for task {}", taskType, ex);
            result = null;
        }

        if (!valid(result, request)) {
            log.warn("LLM output rejected for task {}, using template. Raw: {}", taskType, abbreviate(raw));
            try {
                result = fallback.get();
                modelIdentifier = RussianTemplateGenerator.MODEL_IDENTIFIER;
                status = AiGenerationStatus.FALLBACK;
            } catch (RuntimeException ex) {
                log.error("Fallback generation failed for task {}", taskType, ex);
                AiGeneration failed = AiGeneration.builder()
                        .correlationId(correlationId == null ? UUID.randomUUID() : correlationId)
                        .assessmentId(assessmentId)
                        .modelConfigId(model.getId())
                        .promptTemplateId(prompt.getId())
                        .taskType(taskType)
                        .status(AiGenerationStatus.FAILED)
                        .inputHash(hash)
                        .durationMs((int) Math.min(Integer.MAX_VALUE, System.currentTimeMillis() - started))
                        .errorCode("GENERATION_FAILED")
                        .completedAt(OffsetDateTime.now(ZoneOffset.UTC))
                        .build();
                generationRepository.save(failed);
                throw ex;
            }
        }
        applyMetadata(result, modelIdentifier, prompt.getVersion(), status);

        AiGeneration generation = AiGeneration.builder()
                .correlationId(correlationId == null ? UUID.randomUUID() : correlationId)
                .assessmentId(assessmentId)
                .modelConfigId(model.getId())
                .promptTemplateId(prompt.getId())
                .taskType(taskType)
                .status(status)
                .inputHash(hash)
                .durationMs((int) Math.min(Integer.MAX_VALUE, System.currentTimeMillis() - started))
                .completedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
        generationRepository.saveAndFlush(generation);
        applyGenerationId(result, generation.getId());
        String output = write(result);
        generation.setOutput(output);
        generationRepository.save(generation);
        writeCache(taskType, hash, output);
        return result;
    }

    private boolean valid(Object result, Object request) {
        if (result instanceof CareerExplanationResponseDto explanation) {
            boolean shape = explanation.getSummary() != null && !explanation.getSummary().isBlank()
                    && explanation.getStrengths() != null
                    && explanation.getDevelopmentAreas() != null
                    && explanation.getLimitations() != null;
            if (!shape || !(request instanceof CareerExplanationRequestDto input)) {
                return shape;
            }
            Set<UUID> covered = explanation.getDevelopmentAreas().stream()
                    .map(area -> area.getSkillId())
                    .filter(java.util.Objects::nonNull)
                    .collect(java.util.stream.Collectors.toSet());
            return input.getSkillGaps() == null || input.getSkillGaps().stream()
                    .map(gap -> gap.getSkillId())
                    .filter(java.util.Objects::nonNull)
                    .allMatch(covered::contains);
        }
        if (result instanceof DevelopmentRecommendationResponseDto recommendations) {
            boolean shape = recommendations.getRecommendations() != null
                    && recommendations.getRecommendations().stream().anyMatch(item ->
                    notBlank(item.getTitle()) || notBlank(item.getDescription()));
            if (!shape || !(request instanceof DevelopmentRecommendationRequestDto input)) {
                return shape;
            }
            Set<java.util.UUID> covered = recommendations.getRecommendations().stream()
                    .map(item -> item.getSkillId())
                    .filter(java.util.Objects::nonNull)
                    .collect(java.util.stream.Collectors.toSet());
            return input.getDevelopmentAreas() == null || input.getDevelopmentAreas().stream()
                    .map(area -> area.getSkillId())
                    .filter(java.util.Objects::nonNull)
                    .allMatch(covered::contains);
        }
        if (result instanceof CareerPathInterpretationResponseDto interpretation) {
            return interpretation.getSummary() != null && !interpretation.getSummary().isBlank()
                    && interpretation.getDirections() != null && !interpretation.getDirections().isEmpty();
        }
        if (result instanceof com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto screening) {
            return screening.getSummary() != null && !screening.getSummary().isBlank();
        }
        return false;
    }

    private void applyMetadata(Object result, String modelIdentifier, Integer promptVersion, AiGenerationStatus status) {
        if (result instanceof CareerExplanationResponseDto explanation) {
            explanation.setModelIdentifier(modelIdentifier);
            explanation.setPromptVersion(promptVersion);
            explanation.setStatus(status.name());
        }
        if (result instanceof CareerPathInterpretationResponseDto interpretation) {
            interpretation.setModelIdentifier(modelIdentifier);
            interpretation.setPromptVersion(promptVersion);
        }
        if (result instanceof TalentScreeningExplanationResponseDto screening) {
            screening.setModelIdentifier(modelIdentifier);
            screening.setStatus(status.name());
        }
        if (result instanceof DevelopmentRecommendationResponseDto recommendations) {
            recommendations.setModelIdentifier(modelIdentifier);
        }
    }

    private void applyGenerationId(Object result, UUID id) {
        if (result instanceof CareerExplanationResponseDto explanation) {
            explanation.setGenerationId(id);
        }
        if (result instanceof DevelopmentRecommendationResponseDto recommendations) {
            recommendations.setGenerationId(id);
        }
        if (result instanceof CareerPathInterpretationResponseDto interpretation) {
            interpretation.setGenerationId(id);
        }
        if (result instanceof TalentScreeningExplanationResponseDto screening) {
            screening.setGenerationId(id);
        }
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    private String abbreviate(String raw) {
        if (raw == null) {
            return "";
        }
        String compact = raw.replaceAll("\\s+", " ").trim();
        return compact.length() <= 1000 ? compact : compact.substring(0, 1000);
    }

    private <T> T readCache(String taskType, String hash, Class<T> type) {
        Cache cache = cacheManager.getCache("generations");
        if (cache == null) {
            return null;
        }
        String cached = cache.get(taskType + ":" + hash, String.class);
        return cached == null ? null : read(cached, type);
    }

    private void writeCache(String taskType, String hash, String output) {
        Cache cache = cacheManager.getCache("generations");
        if (cache != null) {
            cache.put(taskType + ":" + hash, output);
        }
    }

    private <T> T read(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json, type);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    private String write(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JacksonException ex) {
            throw new IllegalStateException("Не удалось сериализовать генерацию", ex);
        }
    }

    private String sha256(String value) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
