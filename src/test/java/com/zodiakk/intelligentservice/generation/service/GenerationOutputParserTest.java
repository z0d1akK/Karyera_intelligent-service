package com.zodiakk.intelligentservice.generation.service;

import com.zodiakk.intelligentservice.career.dto.response.CareerExplanationResponseDto;
import com.zodiakk.intelligentservice.common.enums.RecommendationType;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationResponseDto;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenerationOutputParserTest {

    private final GenerationOutputParser parser = new GenerationOutputParser(new JsonMapper());

    @Test
    void acceptsStringDevelopmentAreas() {
        CareerExplanationResponseDto result = parser.parse(GenerationService.CAREER_EXPLANATION, """
                {"summary":"Оценка сохранена","strengths":["Java"],"developmentAreas":["Подтянуть Spring"],"limitations":["Не решение"]}
                """, CareerExplanationResponseDto.class);

        assertEquals("Оценка сохранена", result.getSummary());
        assertEquals("Подтянуть Spring", result.getDevelopmentAreas().getFirst().getExplanation());
    }

    @Test
    void acceptsStringRecommendationsAndSkillNameTitles() {
        UUID skillId = UUID.fromString("9a1b0c2d-3e4f-4a5b-8c7d-000000000060");
        DevelopmentRecommendationResponseDto result = parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, """
                {"recommendations":[{"skillId":"%s","skillName":"Java","text":"Разобрать Spring Data"}]}
                """.formatted(skillId), DevelopmentRecommendationResponseDto.class);

        assertNotNull(result);
        assertEquals(skillId, result.getRecommendations().getFirst().getSkillId());
        assertEquals("Разобрать Spring Data", result.getRecommendations().getFirst().getTitle());
        assertEquals(RecommendationType.SELF_STUDY, result.getRecommendations().getFirst().getType());
    }

    @Test
    void rejectsRecommendationsWithoutText() {
        DevelopmentRecommendationResponseDto result = parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, """
                {"recommendations":[{"skillId":"9a1b0c2d-3e4f-4a5b-8c7d-000000000060"}]}
                """, DevelopmentRecommendationResponseDto.class);

        assertNull(result);
    }
}
