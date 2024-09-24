package dev.mirrex.serviceTests;

import dev.mirrex.dto.request.PutUserRequest;
import dev.mirrex.dto.response.PutUserResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.entities.User;
import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.mappers.UserMapper;
import dev.mirrex.repositories.UserRepository;
import dev.mirrex.services.impl.UserServiceImpl;
import dev.mirrex.util.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testReplaceUser_whenUserExists() {
        PutUserRequest putUserRequest = new PutUserRequest();
        putUserRequest.setEmail("newemail@example.com");

        User currentUser = new User();
        currentUser.setEmail("oldemail@example.com");

        User updatedUser = new User();
        PutUserResponse putUserResponse = new PutUserResponse();

        when(authentication.getName()).thenReturn("testuser@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(currentUser));
        when(userRepository.existsByEmail(putUserRequest.getEmail())).thenReturn(false);
        when(userRepository.save(currentUser)).thenReturn(updatedUser);
        when(userMapper.toReplacedUser(updatedUser)).thenReturn(putUserResponse);

        CustomSuccessResponse<PutUserResponse> response = userService.replaceUser(putUserRequest);

        assertNotNull(response);
        assertEquals(putUserResponse, response.getData());

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(currentUser);
        verify(userMapper, times(1)).toReplacedUser(updatedUser);
    }

    @Test
    void testReplaceUser_whenEmailAlreadyExists() {
        PutUserRequest putUserRequest = new PutUserRequest();
        putUserRequest.setEmail("existingemail@example.com");

        User currentUser = new User();
        currentUser.setEmail("oldemail@example.com");

        when(authentication.getName()).thenReturn("testuser@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(currentUser));
        when(userRepository.existsByEmail(putUserRequest.getEmail())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.replaceUser(putUserRequest);
        });

        assertEquals(ErrorCode.USER_WITH_THIS_EMAIL_ALREADY_EXIST, exception.getErrorCode());

        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toReplacedUser(any());
    }
}
