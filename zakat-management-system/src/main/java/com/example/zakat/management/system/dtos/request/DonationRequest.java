package com.example.zakat.management.system.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DonationRequest {

    @NotNull
    private Long UserId;

    @NotNull
    @DecimalMin(value = "1.00", message = "Donation amount must be at least 1.00")
    private BigDecimal amount;
}
