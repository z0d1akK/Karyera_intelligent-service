package com.zodiakk.intelligentservice.generation.dto.response;

import com.zodiakk.intelligentservice.common.enums.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiGenerationResponseDto {

    @Schema(description = "Id")
    private UUID id;

    @Schema(description = "Created at")
    private OffsetDateTime createdAt;

    @Schema(description = "Correlation id")
    private UUID correlationId;

    @Schema(description = "Assessment id")
    private UUID assessmentId;

    @Schema(description = "Model config id")
    private UUID modelConfigId;

    @Schema(description = "Prompt template id")
    private UUID promptTemplateId;

    @Schema(description = "Task type")
    private String taskType;

    @Schema(description = "Status")
    private AiGenerationStatus status;

    @Schema(description = "Input hash")
    private String inputHash;

    @Schema(description = "Idempotency key")
    private String idempotencyKey;

    @Schema(description = "Input tokens")
    private Integer inputTokens;

    @Schema(description = "Output tokens")
    private Integer outputTokens;

    @Schema(description = "Duration ms")
    private Integer durationMs;

    @Schema(description = "Error code")
    private String errorCode;

    @Schema(description = "Completed at")
    private OffsetDateTime completedAt;
}
