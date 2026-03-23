package com.example.zakat.management.system.dtos.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class ReceiptResponse {
    private Long id;
    private Long donationId;
    private String receiptNumber;
    private String donorName;
    private BigDecimal amount;
    private Instant issuedAt;
}
