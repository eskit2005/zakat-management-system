package com.example.zakat.management.system.mappers;

import com.example.zakat.management.system.dtos.response.UserResponse;
import com.example.zakat.management.system.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role", target = "role")
    UserResponse toResponse(User user);
}
