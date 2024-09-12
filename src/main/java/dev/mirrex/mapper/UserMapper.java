package dev.mirrex.mapper;

import dev.mirrex.dto.request.RegisterUserDto;
import dev.mirrex.dto.request.LoginUserDto;
import dev.mirrex.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterUserDto registerUserDto);

    LoginUserDto toLoginUserDto(User user);
}