package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.InventoryResponse;
import com.example.zakat.management.system.entities.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryResponse toResponse(Inventory inventory);
}