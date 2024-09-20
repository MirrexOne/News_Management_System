package dev.mirrex.exceptionHandlers;

import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.util.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ErrorCode> errorCodes = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ErrorCode.fromMessage(error.getDefaultMessage()))
                .distinct()
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleMultipartException(MultipartException ex, HttpServletRequest request) {
        List<Integer> codes = List.of(ErrorCode.UNKNOWN.getCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<ErrorCode> errorCodes = ex.getConstraintViolations().stream()
                .map(violation -> ErrorCode.fromMessage(violation.getMessage()))
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        List<Integer> codes = List.of(ErrorCode.UNAUTHORISED.getCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleException(CustomException ex, HttpServletRequest request) {
        List<Integer> codes = List.of(ex.getErrorCode().getCode());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(List.of(ErrorCode.UNKNOWN.getCode()), true));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(
                        List.of(ErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode()), true));
    }
}
