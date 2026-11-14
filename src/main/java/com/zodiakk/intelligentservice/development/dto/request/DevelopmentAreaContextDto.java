package com.zodiakk.intelligentservice.development.dto.request;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopmentAreaContextDto {

    @Schema(description = "Skill identifier")
    @NotNull(message = ValidationMessages.SKILL_ID_REQUIRED)
    private UUID skillId;

    @Schema(description = "Skill name used as safe context for the model", example = "Kafka")
    @NotBlank(message = ValidationMessages.SKILL_NAME_REQUIRED)
    @Size(max = 150, message = ValidationMessages.SKILL_NAME_SIZE)
    private String skillName;

    @Schema(description = "Current skill level", example = "1")
    @NotNull(message = ValidationMessages.CURRENT_LEVEL_REQUIRED)
    @Min(value = 0, message = ValidationMessages.CURRENT_LEVEL_MIN)
    @Max(value = 10, message = ValidationMessages.CURRENT_LEVEL_MAX)
    private Integer currentLevel;

    @Schema(description = "Target skill level", example = "3")
    @NotNull(message = ValidationMessages.TARGET_LEVEL_REQUIRED)
    @Min(value = 0, message = ValidationMessages.TARGET_LEVEL_MIN)
    @Max(value = 10, message = ValidationMessages.TARGET_LEVEL_MAX)
    private Integer targetLevel;

    @Schema(description = "Priority score calculated by Analytics Service", example = "0.1")
    @NotNull(message = ValidationMessages.PRIORITY_SCORE_REQUIRED)
    @DecimalMin(value = "0.0", message = ValidationMessages.PRIORITY_SCORE_MIN)
    @DecimalMax(value = "1.0", message = ValidationMessages.PRIORITY_SCORE_MAX)
    private BigDecimal priorityScore;
}
