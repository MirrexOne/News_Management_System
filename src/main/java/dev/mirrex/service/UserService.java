package dev.mirrex.service;

import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.request.LoginUserRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserViewResponse;
import java.util.List;

public interface UserService {

    CustomSuccessResponse<LoginUserRequest> registerUser(RegisterUserRequest registerUserRequest);

    CustomSuccessResponse<LoginUserRequest> loginUser(AuthRequest authDto);

    CustomSuccessResponse<List<PublicUserViewResponse>> getAllUsers();

    CustomSuccessResponse<PublicUserViewResponse> getUserInfoById(Long id);

    CustomSuccessResponse<PublicUserViewResponse> getUserInfo();
}
