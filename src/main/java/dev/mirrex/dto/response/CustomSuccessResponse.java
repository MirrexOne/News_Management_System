package dev.mirrex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CustomSuccessResponse<T> {

    private T data;

    private int statusCode = 0;

    private boolean success = true;

    public CustomSuccessResponse(T data) {
        this.data = data;
    }
}
