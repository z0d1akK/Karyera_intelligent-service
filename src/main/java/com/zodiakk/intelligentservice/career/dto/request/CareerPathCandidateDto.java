package com.zodiakk.intelligentservice.career.dto.request;

import com.zodiakk.intelligentservice.common.dto.SkillGapContextDto;
import com.zodiakk.intelligentservice.common.dto.SkillStrengthDto;
import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerPathCandidateDto {

    @Schema(description = "Target position identifier")
    @NotNull(message = ValidationMessages.TARGET_POSITION_ID_REQUIRED)
    private UUID targetPositionId;

    @Schema(description = "Target position name", example = "Tech Lead")
    @NotBlank(message = ValidationMessages.TARGET_POSITION_NAME_REQUIRED)
    @Size(max = 200, message = ValidationMessages.TARGET_POSITION_NAME_SIZE)
    private String targetPositionName;

    @Schema(description = "Target grade name", example = "Lead")
    @Size(max = 100, message = ValidationMessages.TARGET_GRADE_NAME_SIZE)
    private String targetGradeName;

    @Schema(description = "Normalized total score for the direction", example = "0.78")
    @NotNull(message = ValidationMessages.TOTAL_SCORE_REQUIRED)
    @DecimalMin(value = "0.0", message = ValidationMessages.TOTAL_SCORE_MIN)
    @DecimalMax(value = "1.0", message = ValidationMessages.TOTAL_SCORE_MAX)
    private BigDecimal totalScore;

    @Schema(description = "Whether all mandatory requirements are met for the direction")
    @NotNull(message = ValidationMessages.MANDATORY_REQUIREMENTS_MET_REQUIRED)
    private Boolean mandatoryRequirementsMet;

    @Schema(description = "Skills that meet the requirements")
    @NotNull(message = ValidationMessages.STRENGTHS_REQUIRED)
    @Valid
    private List<SkillStrengthDto> strengths;

    @Schema(description = "Skills that do not meet the requirements")
    @NotNull(message = ValidationMessages.SKILL_GAPS_REQUIRED)
    @Valid
    private List<SkillGapContextDto> skillGaps;
}
