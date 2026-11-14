package com.zodiakk.intelligentservice.career.dto.request;

import com.zodiakk.intelligentservice.common.dto.SkillGapContextDto;
import com.zodiakk.intelligentservice.common.dto.SkillStrengthDto;
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
public class CareerExplanationRequestDto {

    @Schema(description = "Correlation identifier used to trace the generation")
    @NotNull(message = ValidationMessages.CORRELATION_ID_REQUIRED)
    private UUID correlationId;

    @Schema(description = "Assessment identifier")
    @NotNull(message = ValidationMessages.ASSESSMENT_ID_REQUIRED)
    private UUID assessmentId;

    @Schema(description = "Response language", example = "ru")
    @NotBlank(message = ValidationMessages.LANGUAGE_REQUIRED)
    @Size(max = 8, message = ValidationMessages.LANGUAGE_SIZE)
    private String language;

    @Schema(description = "Target position and grade context")
    @NotNull(message = ValidationMessages.TARGET_REQUIRED)
    @Valid
    private CareerTargetDto target;

    @Schema(description = "Assessment scores")
    @NotNull(message = ValidationMessages.SCORES_REQUIRED)
    @Valid
    private AssessmentScoresDto scores;

    @Schema(description = "Whether all mandatory requirements are met")
    @NotNull(message = ValidationMessages.MANDATORY_REQUIREMENTS_MET_REQUIRED)
    private Boolean mandatoryRequirementsMet;

    @Schema(description = "Data quality status provided by Analytics Service", example = "COMPLETE")
    @NotBlank(message = ValidationMessages.DATA_QUALITY_STATUS_REQUIRED)
    @Size(max = 30, message = ValidationMessages.DATA_QUALITY_STATUS_SIZE)
    private String dataQualityStatus;

    @Schema(description = "Skills that meet the requirements")
    @NotNull(message = ValidationMessages.STRENGTHS_REQUIRED)
    @Valid
    private List<SkillStrengthDto> strengths;

    @Schema(description = "Skills that do not meet the requirements")
    @NotNull(message = ValidationMessages.SKILL_GAPS_REQUIRED)
    @Valid
    private List<SkillGapContextDto> skillGaps;
}
