package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.BeneficiaryResponse;
import com.example.zakat.management.system.entities.Beneficiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BeneficiaryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(expression = "java(beneficiary.getEligibilityCheck() != null ? beneficiary.getEligibilityCheck().getIsEligible() : null)", target = "isEligible")
    @Mapping(expression = "java(beneficiary.getZakatAssignment() != null)", target = "hasReceivedZakat")
    BeneficiaryResponse toResponse(Beneficiary beneficiary);
}
