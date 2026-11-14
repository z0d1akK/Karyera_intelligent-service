package com.zodiakk.intelligentservice.career.dto.request;

import com.zodiakk.intelligentservice.common.validation.ValidationMessages;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerPathInterpretationRequestDto {

    @Schema(description = "Correlation identifier used to trace the generation")
    private UUID correlationId;

    @Schema(description = "Employee identifier")
    @NotNull(message = ValidationMessages.EMPLOYEE_ID_REQUIRED)
    private UUID employeeId;

    @Schema(description = "Response language", example = "ru")
    @NotBlank(message = ValidationMessages.LANGUAGE_REQUIRED)
    @Size(max = 8, message = ValidationMessages.LANGUAGE_SIZE)
    private String language;

    @Schema(description = "Current position name", example = "Middle Backend Developer")
    @Size(max = 200, message = ValidationMessages.CURRENT_POSITION_NAME_SIZE)
    private String currentPositionName;

    @Schema(description = "Current grade name", example = "Middle")
    @Size(max = 100, message = ValidationMessages.CURRENT_GRADE_NAME_SIZE)
    private String currentGradeName;

    @Schema(description = "Allowed career directions prepared by Career & Talent Service")
    @NotEmpty(message = ValidationMessages.DIRECTIONS_NOT_EMPTY)
    @Valid
    private List<CareerPathCandidateDto> directions;
}
