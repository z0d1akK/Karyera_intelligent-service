package com.zodiakk.intelligentservice.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillGapContextDto {

    @Schema(description = "Skill identifier")
    private UUID skillId;

    @Schema(description = "Skill name used as safe context for the model")
    private String skillName;

    @Schema(description = "Current skill level")
    private Integer actualLevel;

    @Schema(description = "Required skill level")
    private Integer requiredLevel;

    @Schema(description = "Priority score calculated by Analytics Service")
    private BigDecimal priorityScore;

    @Schema(description = "Whether the skill is a mandatory requirement")
    private Boolean mandatory;
}
