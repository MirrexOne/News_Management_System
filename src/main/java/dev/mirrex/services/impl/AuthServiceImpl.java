package dev.mirrex.services.impl;

import dev.mirrex.configurations.JwtTokenProvider;
import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.response.LoginUserResponse;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.mappers.UserMapper;
import dev.mirrex.entities.User;
import dev.mirrex.repositories.UserRepository;
import dev.mirrex.services.AuthService;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserMapper userMapper;

    @Override
    public CustomSuccessResponse<LoginUserResponse> registerUser(RegisterUserRequest registerUserRequest) {
        logger.debug("Attempting to register user: {}", registerUserRequest.getEmail());
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            logger.warn("Registration failed: User already exists with email: {}", registerUserRequest.getEmail());
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(registerUserRequest);
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully: {}", savedUser.getEmail());

        LoginUserResponse loginUserResponse = userMapper.toLoginUserDto(savedUser);
        loginUserResponse.setToken(jwtTokenProvider.generateToken(savedUser.getEmail()));

        return new CustomSuccessResponse<>(loginUserResponse);
    }

    @Override
    public CustomSuccessResponse<LoginUserResponse> loginUser(AuthRequest authDto) {
        logger.debug("Attempting to login user: {}", authDto.getEmail());
        User user = userRepository.findByEmail(authDto.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Login failed: User not found with email: {}", authDto.getEmail());
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Invalid password for user: {}", authDto.getEmail());
            throw new CustomException(ErrorCode.PASSWORD_NOT_VALID);
        }

        LoginUserResponse loginUserDto = userMapper.toLoginUserDto(user);
        loginUserDto.setToken(jwtTokenProvider.generateToken(user.getEmail()));

        logger.info("User logged in successfully: {}", user.getEmail());
        return new CustomSuccessResponse<>(loginUserDto);
    }
}
