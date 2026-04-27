package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ZakatAssignmentResponse {
    private Long id;
    private BigDecimal amountAssigned;
    private Long invenId;
    private Instant assignedAt;
}