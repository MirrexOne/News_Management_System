package dev.mirrex.interceptor;

import dev.mirrex.services.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    private final LoggingService loggingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication != null ? authentication.getName() : "anonymous";

        loggingService.logInfo(
                "Request processed",
                handler.getClass().getName(),
                request.getMethod(),
                userId,
                request.getRequestURI(),
                request.getMethod(),
                response.getStatus()
        );
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication != null ? authentication.getName() : "anonymous";

            loggingService.logError(
                    "Exception occurred: " + ex.getMessage(),
                    handler.getClass().getName(),
                    request.getMethod(),
                    userId,
                    request.getRequestURI(),
                    request.getMethod(),
                    response.getStatus()
            );
        }
    }
}
