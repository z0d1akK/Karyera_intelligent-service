package com.zodiakk.intelligentservice.prompt.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptTemplateResponseDto {

    @Schema(description = "Id")
    private UUID id;

    @Schema(description = "Created at")
    private OffsetDateTime createdAt;

    @Schema(description = "Code")
    private String code;

    @Schema(description = "Version")
    private Integer version;

    @Schema(description = "Task type")
    private String taskType;

    @Schema(description = "System prompt")
    private String systemPrompt;

    @Schema(description = "User prompt template")
    private String userPromptTemplate;

    @Schema(description = "Output schema version")
    private String outputSchemaVersion;

    @Schema(description = "Active")
    private Boolean active;
}
