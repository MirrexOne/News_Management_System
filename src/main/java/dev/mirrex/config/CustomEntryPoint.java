package dev.mirrex.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.util.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        CustomSuccessResponse<Void> errorResponse = new CustomSuccessResponse<>(
                List.of(ErrorCode.UNAUTHORISED.getCode()), true);

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
