package dev.mirrex.service;

import dev.mirrex.dto.request.LoginUserRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.response.CustomSuccessResponse;

public interface UserService {

    CustomSuccessResponse<LoginUserRequest> registerUser(RegisterUserRequest registerUserRequest);
}
