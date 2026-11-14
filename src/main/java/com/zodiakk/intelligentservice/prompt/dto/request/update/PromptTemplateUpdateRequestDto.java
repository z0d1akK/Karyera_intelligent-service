package com.zodiakk.intelligentservice.prompt.dto.request.update;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptTemplateUpdateRequestDto {

    @Schema(description = "Task type")
    @Size(max = 50, message = ValidationMessages.TASK_TYPE_SIZE)
    private String taskType;

    @Schema(description = "System prompt")
    private String systemPrompt;

    @Schema(description = "User prompt template")
    private String userPromptTemplate;

    @Schema(description = "Output schema version")
    @Size(max = 20, message = ValidationMessages.OUTPUT_SCHEMA_VERSION_SIZE)
    private String outputSchemaVersion;

    @Schema(description = "Active")
    private Boolean active;
}
