package dev.mirrex.service;

import dev.mirrex.config.JwtTokenProvider;
import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.LoginUserRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.exception.CustomException;
import dev.mirrex.mapper.UserMapper;
import dev.mirrex.model.User;
import dev.mirrex.repository.UserRepository;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    @Override
    public CustomSuccessResponse<LoginUserRequest> registerUser(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmail(registerUserRequest.getEmail())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(registerUserRequest);
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));

        User savedUser = userRepository.save(user);
        LoginUserRequest loginUserRequest = userMapper.toLoginUserDto(savedUser);
        loginUserRequest.setToken(jwtTokenProvider.generateToken(savedUser.getEmail()));

        return new CustomSuccessResponse<>(loginUserRequest);
    }

    @Override
    public CustomSuccessResponse<LoginUserRequest> loginUser(AuthRequest authDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(authDto.getEmail())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            LoginUserRequest loginUserDto = userMapper.toLoginUserDto(user);
            loginUserDto.setToken(jwtTokenProvider.generateToken(user.getEmail()));

            return new CustomSuccessResponse<>(loginUserDto);
        } catch (AuthenticationException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
