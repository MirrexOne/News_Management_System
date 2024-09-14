package dev.mirrex.controller;

import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.service.UserServiceImpl;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> getAllUsers() {
        return ResponseEntity.ok()
                .body(userServiceImpl.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoById(
            @PathVariable @NotNull(message = "User ID cannot be null") UUID id) {
        return ResponseEntity.ok()
                .body(userServiceImpl.getUserInfoById(id));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        return ResponseEntity.ok()
                .body(userServiceImpl.getUserInfo());
    }
}
