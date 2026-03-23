package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DistributionHistoryResponse {
    private Long id;
    private Long assignmentId;
    private String beneficiaryName;
    private BigDecimal amountReceived;
    private LocalDate distributionDate;
}
