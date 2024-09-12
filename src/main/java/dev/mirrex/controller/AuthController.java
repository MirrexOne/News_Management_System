package dev.mirrex.controller;

import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.LoginUserRequest;
import dev.mirrex.dto.request.RegisterUserRequest;
import dev.mirrex.service.UserServiceImpl;
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

    private final UserServiceImpl userServiceImpl;

    @PostMapping(value = "/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserRequest>> registerUser(
            @Valid @RequestBody RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok()
                .body(userServiceImpl.registerUser(registerUserRequest));
    }
}
