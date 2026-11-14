package com.zodiakk.intelligentservice.generation.service;

import com.zodiakk.intelligentservice.career.dto.response.CareerExplanationResponseDto;
import com.zodiakk.intelligentservice.career.dto.response.CareerPathInterpretationResponseDto;
import com.zodiakk.intelligentservice.common.enums.RecommendationType;
import com.zodiakk.intelligentservice.development.dto.response.DevelopmentRecommendationResponseDto;
import com.zodiakk.intelligentservice.screening.dto.TalentScreeningExplanationResponseDto;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void parsesInterpretationScreeningAndLooseJsonShapes() {
        assertNull(parser.parse("UNKNOWN", "{}", CareerExplanationResponseDto.class));
        assertNull(parser.parse(GenerationService.CAREER_EXPLANATION, "не json", CareerExplanationResponseDto.class));
        assertNull(parser.parse(GenerationService.CAREER_EXPLANATION, "{\"summary\":\"\"}", CareerExplanationResponseDto.class));

        CareerExplanationResponseDto explanation = parser.parse(GenerationService.CAREER_EXPLANATION, """
                prefix {"summary":"Ок","strengths":"Java","developmentAreas":[{"skillId":"not-uuid","explanation":"Spring"}],"limitations":[{"name":"риск"}]}
                """, CareerExplanationResponseDto.class);
        assertEquals("Ок", explanation.getSummary());
        assertEquals("Java", explanation.getStrengths().getFirst());
        assertEquals("Spring", explanation.getDevelopmentAreas().getFirst().getExplanation());

        DevelopmentRecommendationResponseDto objectItem = parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, """
                {"recommendations":"Прочитать Kafka"}
                """, DevelopmentRecommendationResponseDto.class);
        assertEquals("Прочитать Kafka", objectItem.getRecommendations().getFirst().getTitle());

        CareerPathInterpretationResponseDto interpretation = parser.parse(GenerationService.CAREER_PATH_INTERPRETATION, """
                {"summary":"Сравнение","recommended":"Senior","directions":["коротко",{"targetPositionId":"9a1b0c2d-3e4f-4a5b-8c7d-000000000001","name":"Lead","fitSummary":"сильный"}]}
                """, CareerPathInterpretationResponseDto.class);
        assertEquals("Senior", interpretation.getRecommendedDirection());
        assertEquals(2, interpretation.getDirections().size());

        TalentScreeningExplanationResponseDto screening = parser.parse(GenerationService.TALENT_SCREENING_EXPLANATION, """
                {"summary":"Рейтинг","highlights":["балл"],"limitations":["не решение"]}
                """, TalentScreeningExplanationResponseDto.class);
        assertEquals("Рейтинг", screening.getSummary());
        assertNull(parser.parse(GenerationService.TALENT_SCREENING_EXPLANATION, "{\"summary\":\"\"}", TalentScreeningExplanationResponseDto.class));
    }

    @Test
    void coversRemainingJsonShapes() {
        assertNull(parser.parse(GenerationService.CAREER_EXPLANATION, null, CareerExplanationResponseDto.class));
        assertNull(parser.parse(GenerationService.CAREER_EXPLANATION, "[]", CareerExplanationResponseDto.class));
        assertNull(parser.parse(GenerationService.CAREER_EXPLANATION, "{not-json}", CareerExplanationResponseDto.class));
        assertNull(parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, "{\"recommendations\":null}", DevelopmentRecommendationResponseDto.class));
        assertNull(parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, "{\"recommendations\":[]}", DevelopmentRecommendationResponseDto.class));
        assertNull(parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, "{\"recommendations\":[1]}", DevelopmentRecommendationResponseDto.class));
        assertNull(parser.parse(GenerationService.TALENT_SCREENING_EXPLANATION, "{\"summary\":\"   \"}", TalentScreeningExplanationResponseDto.class));

        CareerExplanationResponseDto areas = parser.parse(GenerationService.CAREER_EXPLANATION, """
                {"summary":"Ок","strengths":[{"title":"Java"}],"developmentAreas":"   ","limitations":[null,""]}
                """, CareerExplanationResponseDto.class);
        assertEquals("Java", areas.getStrengths().getFirst());
        assertTrue(areas.getDevelopmentAreas().isEmpty());

        CareerExplanationResponseDto objectAreas = parser.parse(GenerationService.CAREER_EXPLANATION, """
                {"summary":"Ок","strengths":[],"developmentAreas":{"explanation":""},"limitations":[]}
                """, CareerExplanationResponseDto.class);
        assertTrue(objectAreas.getDevelopmentAreas().isEmpty());

        CareerExplanationResponseDto arrayAreas = parser.parse(GenerationService.CAREER_EXPLANATION, """
                {"summary":"Ок","strengths":[],"developmentAreas":[null,"",{"explanation":"Spring","skillId":"9a1b0c2d-3e4f-4a5b-8c7d-000000000001"}],"limitations":[]}
                """, CareerExplanationResponseDto.class);
        assertEquals(1, arrayAreas.getDevelopmentAreas().size());

        DevelopmentRecommendationResponseDto typed = parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, """
                {"recommendations":[{"title":"Курс","type":"project-experience","expectedOutcome":"навык","estimatedDuration":"2 недели","suggestedActivities":["лабораторная"]}]}
                """, DevelopmentRecommendationResponseDto.class);
        assertEquals(RecommendationType.PROJECT_EXPERIENCE, typed.getRecommendations().getFirst().getType());
        assertEquals("навык", typed.getRecommendations().getFirst().getExpectedOutcome());

        DevelopmentRecommendationResponseDto mentoring = parser.parse(GenerationService.DEVELOPMENT_RECOMMENDATIONS, """
                {"recommendations":[{"description":"Ментор","type":"UNKNOWN"}]}
                """, DevelopmentRecommendationResponseDto.class);
        assertEquals("Ментор", mentoring.getRecommendations().getFirst().getTitle());
        assertEquals(RecommendationType.SELF_STUDY, mentoring.getRecommendations().getFirst().getType());

        CareerPathInterpretationResponseDto interpretation = parser.parse(GenerationService.CAREER_PATH_INTERPRETATION, """
                {"summary":"Сравнение","directions":[null,{"targetPositionId":null}]}
                """, CareerPathInterpretationResponseDto.class);
        assertEquals(1, interpretation.getDirections().size());
        assertNull(parser.parse(GenerationService.CAREER_PATH_INTERPRETATION, "{\"summary\":\"\"}", CareerPathInterpretationResponseDto.class));
    }
}
