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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<List<PublicUserResponse>>> getAllUsers() {
        logger.info("Received request to get all users");
        CustomSuccessResponse<List<PublicUserResponse>> response = userService.getAllUsers();
        logger.info("Returning list of all users, count: {}", response.getData().size());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfoById(
            @PathVariable
            @Pattern(regexp = Constants.UUID_REGEX, message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) UUID id) {
        logger.info("Received request to get user info for ID: {}", id);
        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfoById(id);
        logger.info("Returning user info for ID: {}", id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/info")
    public ResponseEntity<CustomSuccessResponse<PublicUserResponse>> getUserInfo() {
        logger.info("Received request to get current user info");
        CustomSuccessResponse<PublicUserResponse> response = userService.getUserInfo();
        logger.info("Returning current user info");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<BaseSuccessResponse> deleteUser() {
        logger.info("Received request to delete current user");
        BaseSuccessResponse response = userService.deleteUser();
        logger.info("Current user deleted successfully");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<CustomSuccessResponse<PutUserResponse>> replaceUser(
            @RequestBody @Valid PutUserRequest userNewData) {
        logger.info("Received request to update user data");
        CustomSuccessResponse<PutUserResponse> response = userService.replaceUser(userNewData);
        logger.info("User data updated successfully");
        return ResponseEntity.ok().body(response);
    }
}
