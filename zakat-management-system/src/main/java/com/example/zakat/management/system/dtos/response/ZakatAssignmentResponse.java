package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ZakatAssignmentResponse {
    private Long id;
    private Long beneficiaryId;
    private String beneficiaryName;
    private BigDecimal amountAssigned;
    private Instant assignedAt;
}
