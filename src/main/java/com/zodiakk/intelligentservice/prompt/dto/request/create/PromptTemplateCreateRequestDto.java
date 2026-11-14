package com.zodiakk.intelligentservice.prompt.dto.request.create;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptTemplateCreateRequestDto {

    @Schema(description = "Code")
    @NotBlank(message = ValidationMessages.CODE_REQUIRED)
    @Size(max = 80, message = ValidationMessages.CODE_SIZE)
    private String code;

    @Schema(description = "Version")
    @NotNull(message = ValidationMessages.VERSION_REQUIRED)
    private Integer version;

    @Schema(description = "Task type")
    @NotBlank(message = ValidationMessages.TASK_TYPE_REQUIRED)
    @Size(max = 50, message = ValidationMessages.TASK_TYPE_SIZE)
    private String taskType;

    @Schema(description = "System prompt")
    @NotBlank(message = ValidationMessages.SYSTEM_PROMPT_REQUIRED)
    private String systemPrompt;

    @Schema(description = "User prompt template")
    @NotBlank(message = ValidationMessages.USER_PROMPT_TEMPLATE_REQUIRED)
    private String userPromptTemplate;

    @Schema(description = "Output schema version")
    @NotBlank(message = ValidationMessages.OUTPUT_SCHEMA_VERSION_REQUIRED)
    @Size(max = 20, message = ValidationMessages.OUTPUT_SCHEMA_VERSION_SIZE)
    private String outputSchemaVersion;

    @Schema(description = "Active")
    @NotNull(message = ValidationMessages.ACTIVE_REQUIRED)
    private Boolean active;
}
