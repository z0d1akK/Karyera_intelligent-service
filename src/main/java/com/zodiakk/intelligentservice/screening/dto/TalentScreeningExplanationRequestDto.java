package com.zodiakk.intelligentservice.screening.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TalentScreeningExplanationRequestDto {

    private UUID correlationId;
    private UUID reserveId;
    private String locale;
    private String targetPositionName;
    private String targetGradeName;
    private List<Candidate> candidates;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Candidate {
        private UUID employeeId;
        private String employeeName;
        private Integer rank;
        private BigDecimal score;
        private String eligibilityStatus;
        private Boolean mandatoryRequirementsMet;
    }
}
