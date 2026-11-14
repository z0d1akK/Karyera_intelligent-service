package com.zodiakk.intelligentservice.generation.dto.request.update;

import com.zodiakk.intelligentservice.common.enums.*;
import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiGenerationUpdateRequestDto {

    @Schema(description = "Status")
    private AiGenerationStatus status;

    @Schema(description = "Idempotency key")
    @Size(max = 120, message = ValidationMessages.IDEMPOTENCY_KEY_SIZE)
    private String idempotencyKey;

    @Schema(description = "Input tokens")
    private Integer inputTokens;

    @Schema(description = "Output tokens")
    private Integer outputTokens;

    @Schema(description = "Duration ms")
    private Integer durationMs;

    @Schema(description = "Error code")
    @Size(max = 100, message = ValidationMessages.ERROR_CODE_SIZE)
    private String errorCode;

    @Schema(description = "Completed at")
    private OffsetDateTime completedAt;
}
