package com.example.zakat.management.system.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EligibilityCheckRequest {

    @NotNull
    private Long beneficiaryId;

    @NotNull
    @Min(value = 1, message = "Age must be greater than 0")
    private Short age;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal monthlyIncome;

    @NotNull
    @Min(0)
    private Integer dependents;

    @NotBlank
    private String reason;

    @NotNull
    private Boolean hasDebt;

    @NotNull
    private Boolean hasDisability;

    @NotNull
    private Boolean isUnemployed;

    @NotNull
    private Boolean hasChronicIllness;

    @NotNull
    private Boolean isOrphan;
}
