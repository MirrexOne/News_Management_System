package dev.mirrex.controller;

import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.request.LoginUserDtoRequest;
import dev.mirrex.dto.request.RegisterUserDtoRequest;
import dev.mirrex.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomSuccessResponse<LoginUserDtoRequest>> registerUser(
            @Valid @RequestBody RegisterUserDtoRequest registerUserDtoRequest) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userServiceImpl.registerUser(registerUserDtoRequest));
    }
}
