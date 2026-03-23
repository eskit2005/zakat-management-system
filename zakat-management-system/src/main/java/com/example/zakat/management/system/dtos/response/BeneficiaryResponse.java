package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.time.Instant;

@Data
public class BeneficiaryResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private Integer priorityScore;
    private Boolean isEmergency;
    private Instant registeredAt;
    private Boolean isEligible;
    private Boolean hasReceivedZakat;
}
