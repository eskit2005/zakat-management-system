package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.DistributionHistoryResponse;
import com.example.zakat.management.system.entities.DistributionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DistributionHistoryMapper {

    @Mapping(source = "assignment.id", target = "assignmentId")
    DistributionHistoryResponse toResponse(DistributionHistory history);
}
