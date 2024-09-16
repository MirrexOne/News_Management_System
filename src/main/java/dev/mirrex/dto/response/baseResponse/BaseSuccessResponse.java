package dev.mirrex.dto.response.baseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseSuccessResponse {

    private Integer statusCode = 1;

    private Boolean success = true;
}
