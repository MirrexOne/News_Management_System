package dev.mirrex.serviceTests;

import dev.mirrex.security.JwtTokenProvider;
import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.dto.response.LoginUserResponse;
import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.mappers.UserMapper;
import dev.mirrex.entities.User;
import dev.mirrex.repositories.UserRepository;
import dev.mirrex.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_Success() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("test@example.com");

        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setEmail("test@example.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toUser(any(RegisterUserRequest.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toLoginUserDto(any(User.class))).thenReturn(loginUserResponse);
        when(jwtTokenProvider.generateToken(anyString())).thenReturn("token");

        var result = authService.registerUser(request);

        assertNotNull(result);
        assertEquals("test@example.com", result.getData().getEmail());
        assertNotNull(result.getData().getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_UserAlreadyExists() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("existing@example.com");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(CustomException.class, () -> authService.registerUser(request));
    }

    @Test
    void loginUser_Success() {
        AuthRequest request = new AuthRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");

        LoginUserResponse loginUserResponse = new LoginUserResponse();
        loginUserResponse.setEmail("user@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userMapper.toLoginUserDto(any(User.class))).thenReturn(loginUserResponse);
        when(jwtTokenProvider.generateToken(anyString())).thenReturn("token");

        var result = authService.loginUser(request);

        assertNotNull(result);
        assertEquals("user@example.com", result.getData().getEmail());
        assertNotNull(result.getData().getToken());
    }

    @Test
    void loginUser_UserNotFound() {
        AuthRequest request = new AuthRequest();
        request.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> authService.loginUser(request));
    }

    @Test
    void loginUser_InvalidPassword() {
        AuthRequest request = new AuthRequest();
        request.setEmail("user@example.com");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(CustomException.class, () -> authService.loginUser(request));
    }
}
