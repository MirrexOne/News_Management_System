package dev.mirrex.controller;

import dev.mirrex.dto.request.PutUserRequest;
import dev.mirrex.dto.response.PutUserResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.service.UserService;
import dev.mirrex.util.Constants;
import dev.mirrex.util.ValidationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        return ResponseEntity.ok()
                .body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoById(
            @PathVariable
            @Pattern(regexp = Constants.UUID_REGEX, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) UUID id) {
        return ResponseEntity.ok()
                .body(userService.getUserInfoById(id));
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        return ResponseEntity.ok()
                .body(userService.getUserInfo());
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteUser() {
        return ResponseEntity.ok()
                .body(userService.deleteUser());
    }

    @PutMapping
    public ResponseEntity<CustomSuccessResponse<PutUserResponse>> replaceUser(
            @RequestBody @Valid PutUserRequest userNewData) {
        return ResponseEntity.ok()
                .body(userService.replaceUser(userNewData));
    }
}
