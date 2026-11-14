package com.zodiakk.intelligentservice.generation.dto.request.create;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiGenerationCreateRequestDto {

    @Schema(description = "Correlation id")
    @NotNull(message = ValidationMessages.CORRELATION_ID_REQUIRED)
    private UUID correlationId;

    @Schema(description = "Assessment id")
    private UUID assessmentId;

    @Schema(description = "Model config id")
    @NotNull(message = ValidationMessages.MODEL_CONFIG_ID_REQUIRED)
    private UUID modelConfigId;

    @Schema(description = "Prompt template id")
    @NotNull(message = ValidationMessages.PROMPT_TEMPLATE_ID_REQUIRED)
    private UUID promptTemplateId;

    @Schema(description = "Task type")
    @NotBlank(message = ValidationMessages.TASK_TYPE_REQUIRED)
    @Size(max = 50, message = ValidationMessages.TASK_TYPE_SIZE)
    private String taskType;

    @Schema(description = "Idempotency key")
    @Size(max = 120, message = ValidationMessages.IDEMPOTENCY_KEY_SIZE)
    private String idempotencyKey;
}
