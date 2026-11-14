package com.zodiakk.intelligentservice.generation.controller;

import com.zodiakk.intelligentservice.career.dto.request.CareerExplanationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathInterpretationRequestDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerExplanationResponseDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathInterpretationResponseDto;
import com.zodiakk.intelligentservice.common.enums.AiGenerationStatus;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentRecommendationRequestDto;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationResponseDto;
import com.zodiakk.intelligentservice.generation.entity.AiGeneration;
import com.zodiakk.intelligentservice.generation.service.GenerationService;
import com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationRequestDto;
import com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntelligenceControllerTest {

    @Mock
    private GenerationService generationService;

    @InjectMocks
    private IntelligenceController controller;

    @Test
    void delegatesEveryGenerationEndpoint() {
        CareerExplanationRequestDto explanation = new CareerExplanationRequestDto();
        CareerExplanationResponseDto explained = CareerExplanationResponseDto.builder().summary("ok").build();
        when(generationService.explain(explanation)).thenReturn(explained);
        assertEquals("ok", controller.explain(explanation).getSummary());

        DevelopmentRecommendationRequestDto recommend = new DevelopmentRecommendationRequestDto();
        DevelopmentRecommendationResponseDto recommended = DevelopmentRecommendationResponseDto.builder().build();
        when(generationService.recommend(recommend)).thenReturn(recommended);
        assertEquals(recommended, controller.recommend(recommend));

        CareerPathInterpretationRequestDto interpret = new CareerPathInterpretationRequestDto();
        CareerPathInterpretationResponseDto interpreted = CareerPathInterpretationResponseDto.builder().summary("path").build();
        when(generationService.interpret(interpret)).thenReturn(interpreted);
        assertEquals("path", controller.interpret(interpret).getSummary());

        TalentScreeningExplanationRequestDto screening = new TalentScreeningExplanationRequestDto();
        TalentScreeningExplanationResponseDto screened = TalentScreeningExplanationResponseDto.builder().summary("rank").build();
        when(generationService.explainScreening(screening)).thenReturn(screened);
        assertEquals("rank", controller.explainScreening(screening).getSummary());

        UUID id = UUID.randomUUID();
        when(generationService.get(id)).thenReturn(AiGeneration.builder()
                .id(id)
                .status(AiGenerationStatus.FALLBACK)
                .taskType("CAREER_EXPLANATION")
                .build());
        assertEquals(AiGenerationStatus.FALLBACK, controller.generation(id).getStatus());
    }
}
