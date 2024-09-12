package dev.mirrex.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomSuccessResponse<T> {

    private final T data;

    private Integer statusCode = 0;

    private Boolean success = true;

    public CustomSuccessResponse(T data) {
        this.data = data;
    }
}
