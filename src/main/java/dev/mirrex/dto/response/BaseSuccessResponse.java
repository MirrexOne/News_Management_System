package dev.mirrex.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseSuccessResponse {

    private Integer statusCode;

    private Boolean success;
}
