package dev.mirrex.exception;

import dev.mirrex.util.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    private final HttpStatus status;

    public CustomException(ErrorCode errorCode, HttpStatus status) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.status = status;
    }

    public CustomException(ErrorCode errorCode) {
        this(errorCode, HttpStatus.BAD_REQUEST);
    }

    public Integer getCode() {
        return errorCode.getCode();
    }

    public String getMessage() {
        return errorCode.getMessage();
    }
}
