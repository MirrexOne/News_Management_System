package dev.mirrex.mapper;

import dev.mirrex.dto.request.RegisterUserDtoRequest;
import dev.mirrex.dto.request.LoginUserDtoRequest;
import dev.mirrex.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(RegisterUserDtoRequest registerUserDtoRequest);

    @Mapping(target = "token", ignore = true)
    LoginUserDtoRequest toLoginUserDto(User user);
}
