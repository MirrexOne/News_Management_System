package dev.mirrex.services.impl;

import dev.mirrex.entities.Log;
import dev.mirrex.repositories.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import static dev.mirrex.util.Constants.ERROR;
import static dev.mirrex.util.Constants.INFO;

@Service
@AllArgsConstructor
public class LoggingServiceImpl {

    private final LogRepository logRepository;

    public void logInfo(String message, String className, String methodName, String userId, String requestUrl,
                        String requestMethod, Integer responseStatus) {
        Log log = createLog(INFO, message, className, methodName, userId, requestUrl,
                requestMethod, responseStatus);
        logRepository.save(log);
    }

    public void logError(String message, String className, String methodName, String userId, String requestUrl,
                         String requestMethod, Integer responseStatus) {
        Log log = createLog(ERROR, message, className, methodName, userId, requestUrl,
                requestMethod, responseStatus);
        logRepository.save(log);
    }

    private Log createLog(String level, String message, String className, String methodName, String userId,
                          String requestUrl, String requestMethod, Integer responseStatus) {
        Log log = new Log();
        log.setLevel(level);
        log.setMessage(message);
        log.setTimestamp(LocalDateTime.now());
        log.setClassName(className);
        log.setMethodName(methodName);
        log.setUserId(userId);
        log.setRequestUrl(requestUrl);
        log.setRequestMethod(requestMethod);
        log.setResponseStatus(responseStatus);
        return log;
    }
}
