package dev.mirrex.service;

import dev.mirrex.config.JwtTokenProvider;
import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.LoginUserRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.exception.CustomException;
import dev.mirrex.mapper.UserMapper;
import dev.mirrex.model.User;
import dev.mirrex.repository.UserRepository;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .map(userMapper::toPublicUserView)
                .toList();
        return new CustomSuccessResponse<>(users);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfoById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        PublicUserResponse publicUserView = userMapper.toPublicUserView(user);
        return new CustomSuccessResponse<>(publicUserView);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new CustomSuccessResponse<>(userMapper.toPublicUserView(user));
    }
}
