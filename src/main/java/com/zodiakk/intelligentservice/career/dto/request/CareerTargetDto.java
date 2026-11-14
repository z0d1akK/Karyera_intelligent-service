package com.zodiakk.intelligentservice.career.dto.request;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerTargetDto {

    @Schema(description = "Target position name", example = "Senior Backend Developer")
    @NotBlank(message = ValidationMessages.POSITION_NAME_REQUIRED)
    @Size(max = 200, message = ValidationMessages.POSITION_NAME_SIZE)
    private String positionName;

    @Schema(description = "Target grade name", example = "Senior")
    @Size(max = 100, message = ValidationMessages.GRADE_NAME_SIZE)
    private String gradeName;
}
