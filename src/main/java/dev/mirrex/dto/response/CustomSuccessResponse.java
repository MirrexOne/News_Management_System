package dev.mirrex.dto.response;

import lombok.Data;

@Data
public class CustomSuccessResponse<T> {

    private T data;

    private int statusCode = 0;

    private boolean success = true;

    public CustomSuccessResponse(T data) {
        this.data = data;
    }
}
