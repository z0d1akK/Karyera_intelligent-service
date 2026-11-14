package com.zodiakk.intelligentservice.development.dto.request;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationPreferencesDto {

    @Schema(description = "Maximum number of recommendations to generate", example = "5")
    @Min(value = 1, message = ValidationMessages.MAX_RECOMMENDATIONS_MIN)
    @Max(value = 20, message = ValidationMessages.MAX_RECOMMENDATIONS_MAX)
    private Integer maxRecommendations;

    @Schema(description = "Whether project activities may be suggested")
    private Boolean includeProjectActivities;

    @Schema(description = "Whether mentoring may be suggested")
    private Boolean includeMentoring;
}
