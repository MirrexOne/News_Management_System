package dev.mirrex.exceptionHandlers;

import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.util.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import java.util.stream.Collectors;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErrorCode> errorCodes = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ErrorCode.fromMessage(error.getDefaultMessage()))
                .distinct()
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        logger.error("Validation failed: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleMultipartException(MultipartException ex) {
        List<Integer> codes = List.of(ErrorCode.UNKNOWN.getCode());
        logger.error("Multipart request failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorCode> errorCodes = ex.getConstraintViolations().stream()
                .map(violation -> ErrorCode.fromMessage(violation.getMessage()))
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        logger.error("Constraint violation: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleAuthenticationException(AuthenticationException ex) {
        List<Integer> codes = List.of(ErrorCode.UNAUTHORISED.getCode());
        logger.error("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleException(CustomException ex) {
        List<Integer> codes = List.of(ex.getErrorCode().getCode());
        logger.error("Custom exception occurred: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(List.of(ErrorCode.UNKNOWN.getCode()), true));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        logger.error("HTTP message not readable: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(
                        List.of(ErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode()), true));
    }
}
