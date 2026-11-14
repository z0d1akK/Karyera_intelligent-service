package com.zodiakk.intelligentservice.model.dto.request.update;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiModelConfigUpdateRequestDto {

    @Schema(description = "Provider")
    @Size(max = 40, message = ValidationMessages.PROVIDER_SIZE)
    private String provider;

    @Schema(description = "Model name")
    @Size(max = 120, message = ValidationMessages.MODEL_NAME_SIZE)
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
