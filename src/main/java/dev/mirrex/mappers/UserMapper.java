package dev.mirrex.mappers;

import dev.mirrex.dto.request.PutUserRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.response.LoginUserResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.dto.response.PutUserResponse;
import dev.mirrex.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(RegisterUserRequest registerUserRequest);

    @Mapping(target = "token", ignore = true)
    LoginUserResponse toLoginUserDto(User user);

    PublicUserResponse toPublicUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toUser(PublicUserResponse publicUserView);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(PutUserRequest userNewData, @MappingTarget User user);

    PutUserResponse toReplacedUser(User user);
}
