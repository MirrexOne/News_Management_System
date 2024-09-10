package dev.mirrex.controller;

import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.userDto.AuthDto;
import dev.mirrex.dto.userDto.LoginUserDto;
import dev.mirrex.dto.userDto.RegisterUserDto;
import dev.mirrex.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> registerUser(
            @Valid @RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(userService.registerUser(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserDto>> loginUser(
            @Valid @RequestBody AuthDto authDto) {
        return ResponseEntity.ok(userService.loginUser(authDto));
    }
}
