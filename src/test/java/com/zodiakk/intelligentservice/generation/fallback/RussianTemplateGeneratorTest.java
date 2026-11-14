package com.zodiakk.intelligentservice.generation.fallback;

import com.zodiakk.intelligentservice.career.dto.request.AssessmentScoresDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerExplanationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathCandidateDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerPathInterpretationRequestDto;
import com.zodiakk.intelligentservice.career.dto.request.CareerTargetDto;
import com.zodiakk.intelligentservice.common.dto.SkillGapContextDto;
import com.zodiakk.intelligentservice.common.dto.SkillStrengthDto;
import com.zodiakk.intelligentservice.common.enums.RecommendationType;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentAreaContextDto;
import com.zodiakk.intelligentservice.development.dto.request.DevelopmentRecommendationRequestDto;
import com.zodiakk.intelligentservice.development.dto.request.RecommendationPreferencesDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RussianTemplateGeneratorTest {

    private final RussianTemplateGenerator generator = new RussianTemplateGenerator();

    @Test
    void explainsScoresWithoutChangingThem() {
        UUID skillId = UUID.randomUUID();
        var response = generator.explain(CareerExplanationRequestDto.builder()
                .assessmentId(UUID.randomUUID())
                .dataQualityStatus("PARTIAL")
                .mandatoryRequirementsMet(false)
                .target(CareerTargetDto.builder().positionName("Senior").gradeName("Senior").build())
                .scores(AssessmentScoresDto.builder()
                        .skill(new BigDecimal("0.250000"))
                        .experience(BigDecimal.ZERO)
                        .total(new BigDecimal("0.225000"))
                        .build())
                .strengths(List.of(SkillStrengthDto.builder().skillId(skillId).skillName("Java").actualLevel(4).requiredLevel(4).build()))
                .skillGaps(List.of(SkillGapContextDto.builder().skillId(skillId).skillName("Kafka").actualLevel(1).requiredLevel(3).build()))
                .build());

        assertTrue(response.getSummary().contains("22.5%"));
        assertTrue(response.getSummary().contains("Senior"));
        assertTrue(response.getSummary().contains("выполнены не все"));
        assertEquals(skillId, response.getDevelopmentAreas().getFirst().getSkillId());
        assertTrue(response.getLimitations().size() >= 2);
        assertEquals(RussianTemplateGenerator.MODEL_IDENTIFIER, response.getModelIdentifier());
    }

    @Test
    void recommendsOnlyProvidedSkills() {
        UUID skillId = UUID.randomUUID();
        var response = generator.recommend(DevelopmentRecommendationRequestDto.builder()
                .assessmentId(UUID.randomUUID())
                .language("ru")
                .developmentAreas(List.of(
                        DevelopmentAreaContextDto.builder().skillId(skillId).skillName("Kafka").currentLevel(1).targetLevel(3).priorityScore(new BigDecimal("0.10")).build(),
                        DevelopmentAreaContextDto.builder().skillId(UUID.randomUUID()).skillName("Spring").currentLevel(2).targetLevel(4).priorityScore(new BigDecimal("0.05")).build()
                ))
                .preferences(RecommendationPreferencesDto.builder().maxRecommendations(2).includeProjectActivities(false).includeMentoring(true).build())
                .build());

        assertEquals(2, response.getRecommendations().size());
        assertEquals(skillId, response.getRecommendations().getFirst().getSkillId());
        assertEquals(RecommendationType.SELF_STUDY, response.getRecommendations().getFirst().getType());
        assertEquals(RecommendationType.MENTORING, response.getRecommendations().get(1).getType());
        assertFalse(response.getRecommendations().getFirst().getDescription().contains("курс Spring"));
    }

    @Test
    void prefersTheDirectionWithFewerGaps() {
        UUID fewerGaps = UUID.randomUUID();
        var response = generator.interpret(CareerPathInterpretationRequestDto.builder()
                .employeeId(UUID.randomUUID())
                .language("ru")
                .directions(List.of(
                        direction(UUID.randomUUID(), "Architect", "0.90", 2),
                        direction(fewerGaps, "Senior", "0.70", 0)
                ))
                .build());

        assertEquals("Senior", response.getRecommendedDirection());
        assertEquals(fewerGaps, response.getDirections().getFirst().getTargetPositionId());
        assertTrue(response.getRecommendationRationale().contains("не прогнозируется"));
    }

    private CareerPathCandidateDto direction(UUID id, String name, String score, int gaps) {
        return CareerPathCandidateDto.builder()
                .targetPositionId(id)
                .targetPositionName(name)
                .totalScore(new BigDecimal(score))
                .mandatoryRequirementsMet(gaps == 0)
                .strengths(List.of(SkillStrengthDto.builder().skillName("Java").build()))
                .skillGaps(java.util.stream.IntStream.range(0, gaps)
                        .mapToObj(index -> SkillGapContextDto.builder().skillName("Gap " + index).build())
                        .toList())
                .build();
    }
}
