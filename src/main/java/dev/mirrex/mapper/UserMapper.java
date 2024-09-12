package dev.mirrex.mapper;

import dev.mirrex.dto.request.RegisterUserDto;
import dev.mirrex.dto.request.LoginUserDto;
import dev.mirrex.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(RegisterUserDto registerUserDto);

    @Mapping(target = "token", ignore = true)
    LoginUserDto toLoginUserDto(User user);
}