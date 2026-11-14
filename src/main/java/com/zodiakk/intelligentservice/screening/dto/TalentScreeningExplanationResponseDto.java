package com.zodiakk.intelligentservice.screening.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TalentScreeningExplanationResponseDto {

    private UUID generationId;
    private String summary;
    private List<String> highlights;
    private List<String> limitations;
    private String modelIdentifier;
    private String status;
}
