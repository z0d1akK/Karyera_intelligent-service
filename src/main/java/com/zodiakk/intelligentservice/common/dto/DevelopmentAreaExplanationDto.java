package com.zodiakk.intelligentservice.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopmentAreaExplanationDto {

    @Schema(description = "Skill identifier")
    private UUID skillId;

    @Schema(description = "Explanation of the development area")
    private String explanation;
}
