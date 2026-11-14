package com.zodiakk.intelligentservice.development.dto.response;

import com.zodiakk.intelligentservice.common.enums.RecommendationType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopmentRecommendationDto {

    @Schema(description = "Skill identifier")
    private UUID skillId;

    @Schema(description = "Recommendation type")
    private RecommendationType type;

    @Schema(description = "Short recommendation title")
    private String title;

    @Schema(description = "Detailed recommendation description")
    private String description;

    @Schema(description = "Suggested activities")
    private List<String> suggestedActivities;

    @Schema(description = "Expected outcome of the recommendation")
    private String expectedOutcome;

    @Schema(description = "Optional estimated duration")
    private String estimatedDuration;
}
