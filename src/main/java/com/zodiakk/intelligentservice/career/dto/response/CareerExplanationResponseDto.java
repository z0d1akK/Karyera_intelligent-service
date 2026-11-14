package com.zodiakk.intelligentservice.career.dto.response;

import com.zodiakk.intelligentservice.common.dto.DevelopmentAreaExplanationDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerExplanationResponseDto {

    @Schema(description = "Generation identifier")
    private UUID generationId;

    @Schema(description = "Assessment identifier")
    private UUID assessmentId;

    @Schema(description = "Short explanation of the assessment result")
    private String summary;

    @Schema(description = "Strengths confirmed by the assessment")
    private List<String> strengths;

    @Schema(description = "Development areas with explanations")
    private List<DevelopmentAreaExplanationDto> developmentAreas;

    @Schema(description = "Known limitations of the generated explanation")
    private List<String> limitations;

    @Schema(description = "Model identifier used for the generation")
    private String modelIdentifier;

    @Schema(description = "Prompt template version used for the generation")
    private Integer promptVersion;

    @Schema(description = "Generation status")
    private String status;
}
