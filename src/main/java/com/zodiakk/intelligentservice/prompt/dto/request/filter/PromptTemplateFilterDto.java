package com.zodiakk.intelligentservice.prompt.dto.request.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptTemplateFilterDto {

    @Schema(description = "Code")
    private String code;

    @Schema(description = "Version")
    private Integer version;

    @Schema(description = "Task type")
    private String taskType;

    @Schema(description = "Active")
    private Boolean active;
}
