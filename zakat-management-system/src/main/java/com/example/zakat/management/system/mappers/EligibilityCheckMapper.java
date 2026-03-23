package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.EligibilityCheckResponse;
import com.example.zakat.management.system.entities.EligibilityCheck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EligibilityCheckMapper {

    @Mapping(source = "beneficiary.id", target = "beneficiaryId")
    @Mapping(target = "calculatedPriorityScore", ignore = true)
    EligibilityCheckResponse toResponse(EligibilityCheck eligibilityCheck);
}
