package dev.mirrex.service;

import dev.mirrex.dto.request.AuthDto;
import dev.mirrex.dto.request.LoginUserDto;
import dev.mirrex.dto.request.RegisterUserDto;
import dev.mirrex.dto.response.CustomSuccessResponse;

public interface UserService {

    CustomSuccessResponse<LoginUserDto> registerUser(RegisterUserDto registerUserDto);

    CustomSuccessResponse<LoginUserDto> loginUser(AuthDto authDto);
}
