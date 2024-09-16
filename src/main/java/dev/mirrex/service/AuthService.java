package dev.mirrex.service;

import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.response.LoginUserResponse;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;

public interface AuthService {

    CustomSuccessResponse<LoginUserResponse> registerUser(RegisterUserRequest registerUserRequest);

    CustomSuccessResponse<LoginUserResponse> loginUser(AuthRequest authDto);
}
