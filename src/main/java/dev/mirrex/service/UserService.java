package dev.mirrex.service;

import dev.mirrex.config.JwtTokenProvider;
import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.AuthDto;
import dev.mirrex.dto.request.LoginUserDto;
import dev.mirrex.dto.request.RegisterUserDto;
import dev.mirrex.model.User;
import dev.mirrex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    public CustomSuccessResponse<LoginUserDto> registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        User user = User.builder()
                .name(registerUserDto.getName())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .avatar(registerUserDto.getAvatar())
                .role(registerUserDto.getRole())
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtTokenProvider.generateToken(savedUser.getEmail());

        LoginUserDto loginUserDto = new LoginUserDto(
                savedUser.getId().toString(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getAvatar(),
                savedUser.getRole(),
                token
        );

        return new CustomSuccessResponse<>(loginUserDto);
    }

    public CustomSuccessResponse<LoginUserDto> loginUser(AuthDto authDto) {
        User user = userRepository.findByEmail(authDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());

        LoginUserDto loginUserDto = new LoginUserDto(
                user.getId().toString(),
                user.getName(),
                user.getEmail(),
                user.getAvatar(),
                user.getRole(),
                token
        );

        return new CustomSuccessResponse<>(loginUserDto);
    }
}
