package com.zodiakk.intelligentservice.development.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevelopmentRecommendationResponseDto {

    @Schema(description = "Generation identifier")
    private UUID generationId;

    @Schema(description = "Generated development recommendations")
    private List<DevelopmentRecommendationDto> recommendations;

    @Schema(description = "Model identifier")
    private String modelIdentifier;
}
