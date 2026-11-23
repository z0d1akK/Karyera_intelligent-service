package com.zodiakk.intelligentservice.common.dto.field;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Validation field error")
public class ValidationErrorField {

    @Schema(example = "assessmentId")
    private String field;

    @Schema(example = "must not be null")
    private String message;
}
