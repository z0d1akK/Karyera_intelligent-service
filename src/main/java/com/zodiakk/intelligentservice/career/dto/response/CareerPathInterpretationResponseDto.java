package com.zodiakk.intelligentservice.career.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerPathInterpretationResponseDto {

    @Schema(description = "Generation identifier")
    private UUID generationId;

    @Schema(description = "Summary comparing the available career directions")
    private String summary;

    @Schema(description = "Explanation for each career direction")
    private List<CareerPathDirectionExplanationDto> directions;

    @Schema(description = "Direction with the fewest identified gaps")
    private String recommendedDirection;

    @Schema(description = "Rationale for the recommended direction")
    private String recommendationRationale;

    @Schema(description = "Known limitations of the generated interpretation")
    private List<String> limitations;

    @Schema(description = "Model identifier used for the generation")
    private String modelIdentifier;

    @Schema(description = "Prompt template version used for the generation")
    private Integer promptVersion;
}
