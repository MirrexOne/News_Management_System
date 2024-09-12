package dev.mirrex.service;

import dev.mirrex.dto.request.LoginUserDtoRequest;
import dev.mirrex.dto.request.RegisterUserDtoRequest;
import dev.mirrex.dto.response.CustomSuccessResponse;

public interface UserService {

    CustomSuccessResponse<LoginUserDtoRequest> registerUser(RegisterUserDtoRequest registerUserDtoRequest);
}
