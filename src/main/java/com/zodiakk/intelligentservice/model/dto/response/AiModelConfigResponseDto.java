package com.zodiakk.intelligentservice.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiModelConfigResponseDto {

    @Schema(description = "Id")
    private UUID id;

    @Schema(description = "Created at")
    private OffsetDateTime createdAt;

    @Schema(description = "Updated at")
    private OffsetDateTime updatedAt;

    @Schema(description = "Code")
    private String code;

    @Schema(description = "Provider")
    private String provider;

    @Schema(description = "Model name")
    private String modelName;

    @Schema(description = "Temperature")
    private BigDecimal temperature;

    @Schema(description = "Max output tokens")
    private Integer maxOutputTokens;

    @Schema(description = "Timeout seconds")
    private Integer timeoutSeconds;

    @Schema(description = "Active")
    private Boolean active;
}
