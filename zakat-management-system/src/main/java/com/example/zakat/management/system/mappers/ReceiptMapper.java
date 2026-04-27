package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.ReceiptResponse;
import com.example.zakat.management.system.entities.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    @Mapping(source = "recepNum", target = "receiptNumber")
    ReceiptResponse toResponse(Receipt receipt);
}