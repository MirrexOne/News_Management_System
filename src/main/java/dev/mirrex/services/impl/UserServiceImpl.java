package dev.mirrex.services.impl;

import dev.mirrex.dto.request.PutUserRequest;
import dev.mirrex.dto.response.PutUserResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.mappers.UserMapper;
import dev.mirrex.entities.User;
import dev.mirrex.repositories.UserRepository;
import dev.mirrex.services.UserService;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    @Transactional
    public CustomSuccessResponse<PutUserResponse> replaceUser(PutUserRequest userNewData) {
        User currentAuthedUser = getCurrentUser();

        if (!currentAuthedUser.getEmail().equals(userNewData.getEmail()) &&
                userRepository.existsByEmail(userNewData.getEmail())) {
            throw new CustomException(ErrorCode.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
        }

        userMapper.updateUser(userNewData, currentAuthedUser);

        User updatedUser = userRepository.save(currentAuthedUser);
        PutUserResponse replacedUser = userMapper.toReplacedUser(updatedUser);

        return new CustomSuccessResponse<>(replacedUser);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
