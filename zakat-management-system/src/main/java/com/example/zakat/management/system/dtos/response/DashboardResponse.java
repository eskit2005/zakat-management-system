package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardResponse {
    private Long totalDonors;
    private Long totalBeneficiaries;
    private Long pendingEligibility;
    private Long eligibleBeneficiaries;
    private Long assignedBeneficiaries;
    private BigDecimal totalDonated;
    private BigDecimal totalDistributed;
    private BigDecimal remaining;
    private List<DistributionHistoryResponse> recentDistributions;
}
