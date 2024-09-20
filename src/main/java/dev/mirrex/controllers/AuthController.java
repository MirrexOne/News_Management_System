package dev.mirrex.controllers;

import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.LoginUserResponse;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> registerUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest) {
        CustomSuccessResponse<LoginUserResponse> response = authService.registerUser(registerUserRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> loginUser(
            @Valid @RequestBody AuthRequest authRequest) {
        CustomSuccessResponse<LoginUserResponse> response = authService.loginUser(authRequest);
        return ResponseEntity.ok().body(response);
    }
}
