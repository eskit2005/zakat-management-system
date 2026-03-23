package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.ZakatAssignmentResponse;
import com.example.zakat.management.system.entities.ZakatAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ZakatAssignmentMapper {

    @Mapping(source = "beneficiary.id", target = "beneficiaryId")
    @Mapping(source = "beneficiary.fullName", target = "beneficiaryName")
    ZakatAssignmentResponse toResponse(ZakatAssignment assignment);
}
