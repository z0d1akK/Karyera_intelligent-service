package com.zodiakk.intelligentservice.screening.dto.response;

import com.zodiakk.intelligentservice.common.dto.DevelopmentAreaExplanationDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TalentScreeningExplanationResponseDto {

    @Schema(description = "Generation identifier")
    private UUID generationId;

    @Schema(description = "Screening identifier")
    private UUID screeningId;

    @Schema(description = "Candidate employee identifier")
    private UUID employeeId;

    @Schema(description = "Short explanation of the screening result")
    private String summary;

    @Schema(description = "Strengths identified for the candidate")
    private List<String> strengths;

    @Schema(description = "Development areas with explanations")
    private List<DevelopmentAreaExplanationDto> developmentAreas;

    @Schema(description = "Known limitations of the generated explanation")
    private List<String> limitations;

    @Schema(description = "Model identifier used for the generation")
    private String modelIdentifier;

    @Schema(description = "Prompt template version used for the generation")
    private Integer promptVersion;
}
