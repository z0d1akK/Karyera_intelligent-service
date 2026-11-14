package com.zodiakk.intelligentservice.model.dto.request.create;

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
public class AiModelConfigCreateRequestDto {

    @Schema(description = "Code")
    @NotBlank(message = ValidationMessages.CODE_REQUIRED)
    @Size(max = 60, message = ValidationMessages.CODE_SIZE)
    private String code;

    @Schema(description = "Provider")
    @NotBlank(message = ValidationMessages.PROVIDER_REQUIRED)
    @Size(max = 40, message = ValidationMessages.PROVIDER_SIZE)
    private String provider;

    @Schema(description = "Model name")
    @NotBlank(message = ValidationMessages.MODEL_NAME_REQUIRED)
    @Size(max = 120, message = ValidationMessages.MODEL_NAME_SIZE)
    private String modelName;

    @Schema(description = "Temperature")
    @NotNull(message = ValidationMessages.TEMPERATURE_REQUIRED)
    private BigDecimal temperature;

    @Schema(description = "Max output tokens")
    @NotNull(message = ValidationMessages.MAX_OUTPUT_TOKENS_REQUIRED)
    private Integer maxOutputTokens;

    @Schema(description = "Timeout seconds")
    @NotNull(message = ValidationMessages.TIMEOUT_SECONDS_REQUIRED)
    private Integer timeoutSeconds;

    @Schema(description = "Active")
    @NotNull(message = ValidationMessages.ACTIVE_REQUIRED)
    private Boolean active;
}
