package com.zodiakk.intelligentservice.career.dto.request;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentScoresDto {

    @Schema(description = "Normalized skill score", example = "0.825")
    @NotNull(message = ValidationMessages.SKILL_REQUIRED)
    @DecimalMin(value = "0.0", message = ValidationMessages.SKILL_MIN)
    @DecimalMax(value = "1.0", message = ValidationMessages.SKILL_MAX)
    private BigDecimal skill;

    @Schema(description = "Normalized experience score", example = "0.833333")
    @NotNull(message = ValidationMessages.EXPERIENCE_REQUIRED)
    @DecimalMin(value = "0.0", message = ValidationMessages.EXPERIENCE_MIN)
    @DecimalMax(value = "1.0", message = ValidationMessages.EXPERIENCE_MAX)
    private BigDecimal experience;

    @Schema(description = "Normalized total score", example = "0.826667")
    @NotNull(message = ValidationMessages.TOTAL_REQUIRED)
    @DecimalMin(value = "0.0", message = ValidationMessages.TOTAL_MIN)
    @DecimalMax(value = "1.0", message = ValidationMessages.TOTAL_MAX)
    private BigDecimal total;
}
