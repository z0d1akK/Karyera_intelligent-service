package com.zodiakk.intelligentservice.career.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerPathDirectionExplanationDto {

    @Schema(description = "Target position identifier")
    private UUID targetPositionId;

    @Schema(description = "Target position name")
    private String targetPositionName;

    @Schema(description = "Summary of the fit for this direction")
    private String fitSummary;

    @Schema(description = "Skills that already match the direction requirements")
    private List<String> strengths;

    @Schema(description = "Competencies that should be developed for this direction")
    private List<String> developmentAreas;
}
