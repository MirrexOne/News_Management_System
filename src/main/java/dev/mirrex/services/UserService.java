package dev.mirrex.services;

import dev.mirrex.dto.request.PutUserRequest;
import dev.mirrex.dto.response.PutUserResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.entities.User;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.UUID;

public interface UserService {

    CustomSuccessResponse<List<PublicUserResponse>> getAllUsers();

    CustomSuccessResponse<PublicUserResponse> getUserInfoById(UUID id);

    CustomSuccessResponse<PublicUserResponse> getUserInfo();

    BaseSuccessResponse deleteUser();

    CustomSuccessResponse<PutUserResponse> replaceUser(PutUserRequest userNewData);

    User getCurrentUser();
}
