package dev.mirrex.exception;

import dev.mirrex.dto.response.CustomSuccessResponse;
import dev.mirrex.util.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<ErrorCode> errorCodes = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ErrorCode.fromMessage(error.getDefaultMessage()))
                .distinct()
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleCustomException(CustomException ex) {
        List<Integer> codes = List.of(ex.getErrorCode().getCode());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(List.of(ErrorCode.UNKNOWN.getCode()), true));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(List.of(ErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode()), true));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomSuccessResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorCode> errorCodes = ex.getConstraintViolations().stream()
                .map(violation -> ErrorCode.fromMessage(violation.getMessage()))
                .toList();
        List<Integer> codes = errorCodes.stream().map(ErrorCode::getCode).collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(new CustomSuccessResponse<>(codes, true));
    }
}
