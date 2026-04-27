package com.example.zakat.management.system.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class BeneficiaryFormRequest {

    @NotNull
    private Long beneficiaryId;

    private String reason;

    @Positive
    private Integer dependents;

    @Positive
    private BigDecimal income;

    private Boolean emergency;

    private Boolean disability;

    @Positive
    private Integer age;

    private Boolean isOrphan;

    private Boolean hasDebt;

    private Boolean unemployed;

    private Boolean illness;
}