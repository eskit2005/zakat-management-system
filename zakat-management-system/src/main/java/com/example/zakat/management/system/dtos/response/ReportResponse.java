package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ReportResponse {
    private BigDecimal totalDonated;
    private BigDecimal totalDistributed;
    private BigDecimal remaining;
    private Long totalDonors;
    private Long totalBeneficiaries;
    private Long totalAssignments;
}
