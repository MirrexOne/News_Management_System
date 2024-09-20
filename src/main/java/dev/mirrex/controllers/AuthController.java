package dev.mirrex.controllers;

import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.LoginUserResponse;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> registerUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest) {
        logger.info("Received registration request for user: {}", registerUserRequest.getEmail());
        CustomSuccessResponse<LoginUserResponse> response = authService.registerUser(registerUserRequest);
        logger.info("User registered successfully: {}", registerUserRequest.getEmail());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> loginUser(
            @Valid @RequestBody AuthRequest authRequest) {
        logger.info("Received login request for user: {}", authRequest.getEmail());
        CustomSuccessResponse<LoginUserResponse> response = authService.loginUser(authRequest);
        logger.info("User logged in successfully: {}", authRequest.getEmail());
        return ResponseEntity.ok().body(response);
    }
}