package dev.mirrex.interceptor;

import dev.mirrex.services.impl.LoggingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;

import static dev.mirrex.util.Constants.ERROR_OCCURRED;
import static dev.mirrex.util.Constants.REQUEST_COMPLETED;
import static dev.mirrex.util.Constants.ANONYMOUS_USER;

@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private final LoggingServiceImpl loggingService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String userId = SecurityContextHolder.getContext().getAuthentication() != null ?
                SecurityContextHolder.getContext().getAuthentication().getName() : ANONYMOUS_USER;

        if (ex != null) {
            String errorInfo =
                    ERROR_OCCURRED.formatted(
                    ex.getMessage(),
                    handler.getClass().getName(),
                    request.getMethod(),
                    userId,
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus()
            );
            loggingService.logError(errorInfo, handler.getClass().getName(), request.getMethod(), userId,
                    request.getRequestURI(), request.getMethod(), response.getStatus());
        } else {
            String logInfo = REQUEST_COMPLETED.formatted(
                    handler.getClass().getName(),
                    request.getMethod(),
                    userId,
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus()
            );
            loggingService.logInfo(logInfo, handler.getClass().getName(), request.getMethod(), userId,
                    request.getRequestURI(), request.getMethod(), response.getStatus());
        }
    }
}
