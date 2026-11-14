package com.zodiakk.intelligentservice.generation.dto.request.filter;

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
public class AiGenerationFilterDto {

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

    @Schema(description = "Idempotency key")
    private String idempotencyKey;

    @Schema(description = "Creation time from (inclusive)")
    private OffsetDateTime createdAtFrom;

    @Schema(description = "Creation time to (inclusive)")
    private OffsetDateTime createdAtTo;
}
