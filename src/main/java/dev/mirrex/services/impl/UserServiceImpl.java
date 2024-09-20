package dev.mirrex.services.impl;

import dev.mirrex.dto.request.PutUserRequest;
import dev.mirrex.dto.response.PutUserResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.dto.response.PublicUserResponse;
import dev.mirrex.entities.News;
import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.mappers.UserMapper;
import dev.mirrex.entities.User;
import dev.mirrex.repositories.UserRepository;
import dev.mirrex.services.UserService;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public CustomSuccessResponse<List<PublicUserResponse>> getAllUsers() {
        logger.info("Fetching all users");
        List<PublicUserResponse> users = userRepository.findAll()
                .stream()
                .map(userMapper::toPublicUserResponse)
                .toList();
        logger.info("Found {} users", users.size());
        return new CustomSuccessResponse<>(users);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfoById(UUID id) {
        logger.info("Fetching user info for ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", id);
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        PublicUserResponse publicUserView = userMapper.toPublicUserResponse(user);
        logger.info("User info retrieved for ID: {}", id);
        return new CustomSuccessResponse<>(publicUserView);
    }

    @Override
    public CustomSuccessResponse<PublicUserResponse> getUserInfo() {
        logger.info("Fetching current user info");
        User currentAuthedUser = getCurrentUser();
        logger.info("Current user info retrieved for ID: {}", currentAuthedUser.getId());
        return new CustomSuccessResponse<>(userMapper.toPublicUserResponse(currentAuthedUser));
    }

    @Override
    public BaseSuccessResponse deleteUser() {
        logger.info("Deleting current user");
        User currentAuthedUser = getCurrentUser();
        userRepository.delete(currentAuthedUser);
        logger.info("User deleted successfully, ID: {}", currentAuthedUser.getId());
        return new BaseSuccessResponse();
    }

    @Override
    @Transactional
    public CustomSuccessResponse<PutUserResponse> replaceUser(PutUserRequest userNewData) {
        logger.info("Updating user data");
        User currentAuthedUser = getCurrentUser();

        if (!currentAuthedUser.getEmail().equals(userNewData.getEmail()) &&
                userRepository.existsByEmail(userNewData.getEmail())) {
            logger.warn("Update failed: User already exists with email: {}", userNewData.getEmail());
            throw new CustomException(ErrorCode.USER_WITH_THIS_EMAIL_ALREADY_EXIST);
        }

        userMapper.updateUser(userNewData, currentAuthedUser);

        User updatedUser = userRepository.save(currentAuthedUser);
        PutUserResponse replacedUser = userMapper.toReplacedUser(updatedUser);

        logger.info("User data updated successfully for ID: {}", updatedUser.getId());
        return new CustomSuccessResponse<>(replacedUser);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Current authenticated user not found in database: {}", email);
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });
    }

    @Override
    public Boolean hasAccessToResource(News news, User currentUser) {
        return news.getAuthor().getId().equals(currentUser.getId());
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }
}
