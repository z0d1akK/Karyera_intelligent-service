package com.zodiakk.intelligentservice.screening.dto.request;

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
public class TalentScreeningExplanationRequestDto {

    @Schema(description = "Correlation identifier used to trace the generation")
    private UUID correlationId;

    @Schema(description = "Screening identifier")
    @NotNull(message = ValidationMessages.SCREENING_ID_REQUIRED)
    private UUID screeningId;

    @Schema(description = "Candidate employee identifier")
    @NotNull(message = ValidationMessages.EMPLOYEE_ID_REQUIRED)
    private UUID employeeId;

    @Schema(description = "Response language", example = "ru")
    @NotBlank(message = ValidationMessages.LANGUAGE_REQUIRED)
    @Size(max = 8, message = ValidationMessages.LANGUAGE_SIZE)
    private String language;

    @Schema(description = "Target position name", example = "Senior Backend Developer")
    @NotBlank(message = ValidationMessages.TARGET_POSITION_NAME_REQUIRED)
    @Size(max = 200, message = ValidationMessages.TARGET_POSITION_NAME_SIZE)
    private String targetPositionName;

    @Schema(description = "Target grade name", example = "Senior")
    @Size(max = 100, message = ValidationMessages.TARGET_GRADE_NAME_SIZE)
    private String targetGradeName;

    @Schema(description = "Candidate rank in the screening", example = "2")
    @NotNull(message = ValidationMessages.RANK_REQUIRED)
    @Min(value = 1, message = ValidationMessages.RANK_MIN)
    private Integer rank;

    @Schema(description = "Total number of evaluated candidates", example = "12")
    @NotNull(message = ValidationMessages.CANDIDATE_COUNT_REQUIRED)
    @Min(value = 1, message = ValidationMessages.CANDIDATE_COUNT_MIN)
    private Integer candidateCount;

    @Schema(description = "Normalized total score", example = "0.78")
    @NotNull(message = ValidationMessages.SCORE_REQUIRED)
    @DecimalMin(value = "0.0", message = ValidationMessages.SCORE_MIN)
    @DecimalMax(value = "1.0", message = ValidationMessages.SCORE_MAX)
    private BigDecimal score;

    @Schema(description = "Eligibility status calculated by Analytics Service", example = "ELIGIBLE")
    @NotBlank(message = ValidationMessages.ELIGIBILITY_STATUS_REQUIRED)
    @Size(max = 30, message = ValidationMessages.ELIGIBILITY_STATUS_SIZE)
    private String eligibilityStatus;

    @Schema(description = "Skills that meet the requirements")
    @NotNull(message = ValidationMessages.STRENGTHS_REQUIRED)
    @Valid
    private List<SkillStrengthDto> strengths;

    @Schema(description = "Skills that do not meet the requirements")
    @NotNull(message = ValidationMessages.SKILL_GAPS_REQUIRED)
    @Valid
    private List<SkillGapContextDto> skillGaps;
}
