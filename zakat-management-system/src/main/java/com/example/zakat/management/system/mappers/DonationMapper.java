package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.DonationResponse;
import com.example.zakat.management.system.entities.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ReceiptMapper.class)
public interface DonationMapper {

    @Mapping(source = "donor.id", target = "userId")
    @Mapping(source = "donor.name", target = "donorName")
    @Mapping(source = "receipt", target = "receipt")
    DonationResponse toResponse(Donation donation);
}
