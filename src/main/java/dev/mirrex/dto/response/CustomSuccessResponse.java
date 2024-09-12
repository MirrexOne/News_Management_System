package dev.mirrex.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CustomSuccessResponse<T> extends BaseSuccessResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Integer> codes;

    public CustomSuccessResponse(T data) {
        super(1, true);
        this.data = data;
    }

    public CustomSuccessResponse(List<Integer> codes, boolean success) {
        super(getStatusCode(codes), success);
        this.codes = codes;
    }

    private static int getStatusCode(List<Integer> codes) {
        return codes.isEmpty() ? 0 : codes.getFirst();
    }
}
