package dev.mirrex.controller;

import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.LoginUserDto;
import dev.mirrex.dto.request.RegisterUserDto;
import dev.mirrex.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> registerUser(
            @Valid @RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(userServiceImpl.registerUser(registerUserDto));
    }
}
