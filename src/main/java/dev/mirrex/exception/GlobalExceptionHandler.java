package dev.mirrex.exception;

import dev.mirrex.util.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomException> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ex, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomException> handleGenericException() {
        CustomException customEx = new CustomException(ErrorCode.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(customEx, customEx.getStatus());
    }
}