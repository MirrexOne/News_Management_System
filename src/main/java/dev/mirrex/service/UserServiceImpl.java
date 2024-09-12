package dev.mirrex.service;

import dev.mirrex.config.JwtTokenProvider;
import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.AuthDto;
import dev.mirrex.dto.request.LoginUserDto;
import dev.mirrex.dto.request.RegisterUserDto;
import dev.mirrex.exception.CustomException;
import dev.mirrex.mapper.UserMapper;
import dev.mirrex.model.User;
import dev.mirrex.repository.UserRepository;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    public CustomSuccessResponse<LoginUserDto> registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new CustomException(ErrorCode.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
        }

        User user = userMapper.toUser(registerUserDto);
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        User savedUser = userRepository.save(user);
        LoginUserDto loginUserDto = userMapper.toLoginUserDto(savedUser);
        loginUserDto.setToken(jwtTokenProvider.generateToken(savedUser.getEmail()));

        return new CustomSuccessResponse<>(loginUserDto);
    }

    @Override
    public CustomSuccessResponse<LoginUserDto> loginUser(AuthDto authDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(authDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LoginUserDto loginUserDto = userMapper.toLoginUserDto(user);
        loginUserDto.setToken(jwtTokenProvider.generateToken(user.getEmail()));

        return new CustomSuccessResponse<>(loginUserDto);
    }
}