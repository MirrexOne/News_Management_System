package dev.mirrex.exceptionHandlers;

import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.services.LoggingService;
import dev.mirrex.util.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import java.util.stream.Collectors;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final LoggingService loggingService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorCode> errorCodes = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ErrorCode.fromMessage(error.getDefaultMessage()))
                .distinct()
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        logError("Validation failed: " + ex.getMessage(), request, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleMultipartException(MultipartException ex, HttpServletRequest request) {
        List<Integer> codes = List.of(ErrorCode.UNKNOWN.getCode());
        logError("Multipart request failed: " + ex.getMessage(), request, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ErrorCode> errorCodes = ex.getConstraintViolations().stream()
                .map(violation -> ErrorCode.fromMessage(violation.getMessage()))
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        logError("Constraint violation: " + ex.getMessage(), request, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        List<Integer> codes = List.of(ErrorCode.UNAUTHORISED.getCode());
        logError("Authentication failed: " + ex.getMessage(), request, HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleException(CustomException ex, HttpServletRequest request) {
        List<Integer> codes = List.of(ex.getErrorCode().getCode());
        logError("Custom exception occurred: " + ex.getMessage(), request, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {
        logError("Unexpected error occurred: " + ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(List.of(ErrorCode.UNKNOWN.getCode()), true));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        logError("HTTP message not readable: " + ex.getMessage(), request, HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(
                        List.of(ErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode()), true));
    }

    private void logError(String message, HttpServletRequest request, int status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication != null ? authentication.getName() : "anonymous";

        loggingService.logError(
                message,
                this.getClass().getName(),
                "handleException",
                userId,
                request.getRequestURI(),
                request.getMethod(),
                status
        );
    }
}
