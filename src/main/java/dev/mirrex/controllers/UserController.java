package dev.mirrex.controllers;

import dev.mirrex.dto.request.PutUserRequest;
import dev.mirrex.dto.response.PutUserResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.services.UserService;
import dev.mirrex.util.Constants;
import dev.mirrex.util.ValidationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> getAllUsers() {
        CustomSuccessResponse<List<PublicUserResponse>> response = userService.getAllUsers();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoById(
            @PathVariable
            @Pattern(regexp = Constants.UUID_REGEX, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) UUID id) {
        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfoById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfo();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteUser() {
        BaseSuccessResponse response = userService.deleteUser();
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<CustomSuccessResponse<PutUserResponse>> replaceUser(
            @RequestBody @Valid PutUserRequest userNewData) {
        CustomSuccessResponse<PutUserResponse> response = userService.replaceUser(userNewData);
        return ResponseEntity.ok().body(response);
    }
}
