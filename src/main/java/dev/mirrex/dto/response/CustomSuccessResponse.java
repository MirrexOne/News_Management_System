package dev.mirrex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomSuccessResponse<T> {

    private T data;

    private int statusCode;

    private boolean success;
}
