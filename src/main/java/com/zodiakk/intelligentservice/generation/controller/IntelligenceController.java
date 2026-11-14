package com.zodiakk.intelligentservice.generation.controller;

import com.zodiakk.intelligentservice.career.dto.request.CareerExplanationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathInterpretationRequestDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerExplanationResponseDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathInterpretationResponseDto;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentRecommendationRequestDto;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationResponseDto;
import com.zodiakk.intelligentservice.generation.dto.response.AiGenerationResponseDto;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;
import com.zodiakk.intelligentservice.generation.service.GenerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/v1/intelligence")
@RequiredArgsConstructor
public class IntelligenceController {

    private final GenerationService generationService;

    @PostMapping("/career-explanations")
    public CareerExplanationResponseDto explain(@Valid @RequestBody CareerExplanationRequestDto request) {
        return generationService.explain(request);
    }

    @PostMapping("/development-recommendations")
    public DevelopmentRecommendationResponseDto recommend(@Valid @RequestBody DevelopmentRecommendationRequestDto request) {
        return generationService.recommend(request);
    }

    @PostMapping("/career-path-interpretations")
    public CareerPathInterpretationResponseDto interpret(@Valid @RequestBody CareerPathInterpretationRequestDto request) {
        return generationService.interpret(request);
    }

    @PostMapping("/talent-screening-explanations")
    public com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto explainScreening(
            @Valid @RequestBody com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationRequestDto request
    ) {
        return generationService.explainScreening(request);
    }

    @GetMapping("/generations/{id}")
    public AiGenerationResponseDto generation(@PathVariable UUID id) {
        AiGeneration generation = generationService.get(id);
        return AiGenerationResponseDto.builder()
                .id(generation.getId())
                .createdAt(generation.getCreatedAt())
                .correlationId(generation.getCorrelationId())
                .assessmentId(generation.getAssessmentId())
                .modelConfigId(generation.getModelConfigId())
                .promptTemplateId(generation.getPromptTemplateId())
                .taskType(generation.getTaskType())
                .status(generation.getStatus())
                .inputHash(generation.getInputHash())
                .idempotencyKey(generation.getIdempotencyKey())
                .inputTokens(generation.getInputTokens())
                .outputTokens(generation.getOutputTokens())
                .durationMs(generation.getDurationMs())
                .errorCode(generation.getErrorCode())
                .completedAt(generation.getCompletedAt())
                .build();
    }
}
