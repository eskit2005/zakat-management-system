package com.example.zakat.management.system.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BeneficiaryRegisterRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String fullName;

    private Boolean isEmergency = false;
}
