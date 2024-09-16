package dev.mirrex.controller;

import dev.mirrex.dto.request.AuthRequest;
import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.LoginUserRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.service.AuthService;
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
    public ResponseEntity<CustomSuccessResponse<LoginUserRequest>> registerUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok()
                .body(authService.registerUser(registerUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserRequest>> loginUser(
            @Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok()
                .body(authService.loginUser(authRequest));
    }
}
