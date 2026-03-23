package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class EligibilityCheckResponse {
    private Long id;
    private Long beneficiaryId;
    private Short age;
    private BigDecimal monthlyIncome;
    private Integer dependents;
    private String reason;
    private Boolean hasDebt;
    private Boolean hasDisability;
    private Boolean isUnemployed;
    private Boolean hasChronicIllness;
    private Boolean isOrphan;
    private Boolean isEligible;
    private String rejectionReason;
    private Integer calculatedPriorityScore;
    private Instant checkedAt;
}
