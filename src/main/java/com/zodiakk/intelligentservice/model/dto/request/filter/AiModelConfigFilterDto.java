package com.zodiakk.intelligentservice.model.dto.request.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiModelConfigFilterDto {

    @Schema(description = "Code")
    private String code;

    @Schema(description = "Provider")
    private String provider;

    @Schema(description = "Model name")
    private String modelName;

    @Schema(description = "Active")
    private Boolean active;
}
