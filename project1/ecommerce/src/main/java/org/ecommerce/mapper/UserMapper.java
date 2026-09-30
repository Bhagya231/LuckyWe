package org.ecommerce.mapper;

import org.ecommerce.dto.LoginResponse;
import org.ecommerce.dto.RegisterRequest;
import org.ecommerce.dto.RegisterResponse;
import org.ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    @Mapping(target = "id",ignore = true)
    @Mapping(target = "password",ignore = true)
    @Mapping(target = "role",ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "message", ignore = true)
    RegisterResponse toRegisterResponse(User user);

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "message", ignore = true)
    LoginResponse toLoginResponse(User user);

}
