package dev.mirrex.service.impl;

import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.exception.CustomException;
import dev.mirrex.mapper.UserMapper;
import dev.mirrex.model.User;
import dev.mirrex.repository.UserRepository;
import dev.mirrex.service.UserService;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public CustomSuccessResponse<List<PublicUserResponse>> getAllUsers() {
        List<PublicUserResponse> users = userRepository.findAll()
                .stream()
                .map(userMapper::toPublicUserResponse)
                .toList();
        return new CustomSuccessResponse<>(users);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfoById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PublicUserResponse publicUserView = userMapper.toPublicUserResponse(user);
        return new CustomSuccessResponse<>(publicUserView);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfo() {
        User currentAuthedUser = getCurrentUser();
        return new CustomSuccessResponse<>(userMapper.toPublicUserResponse(currentAuthedUser));
    }

    @Override
    public BaseSuccessResponse deleteUser() {
        User currentAuthedUser = getCurrentUser();
        userRepository.delete(currentAuthedUser);
        return new BaseSuccessResponse();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
