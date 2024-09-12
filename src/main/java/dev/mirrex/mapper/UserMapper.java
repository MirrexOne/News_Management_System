package dev.mirrex.mapper;

import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.request.LoginUserRequest;
import dev.mirrex.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(RegisterUserRequest registerUserRequest);

    @Mapping(target = "token", ignore = true)
    LoginUserRequest toLoginUserDto(User user);
}
