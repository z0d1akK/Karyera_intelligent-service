package com.zodiakk.intelligentservice.development.dto.request;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopmentRecommendationRequestDto {

    @Schema(description = "Correlation identifier used to trace the generation")
    private UUID correlationId;

    @Schema(description = "Assessment identifier")
    @NotNull(message = ValidationMessages.ASSESSMENT_ID_REQUIRED)
    private UUID assessmentId;

    @Schema(description = "Response language", example = "ru")
    @NotBlank(message = ValidationMessages.LANGUAGE_REQUIRED)
    @Size(max = 8, message = ValidationMessages.LANGUAGE_SIZE)
    private String language;

    @Schema(description = "Target position name", example = "Senior Backend Developer")
    @Size(max = 200, message = ValidationMessages.TARGET_POSITION_NAME_SIZE)
    private String targetPositionName;

    @Schema(description = "Development areas identified by Analytics Service")
    @NotEmpty(message = ValidationMessages.DEVELOPMENT_AREAS_NOT_EMPTY)
    @Valid
    private List<DevelopmentAreaContextDto> developmentAreas;

    @Schema(description = "Generation preferences")
    @Valid
    private RecommendationPreferencesDto preferences;
}
