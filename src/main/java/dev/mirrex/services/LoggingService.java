package dev.mirrex.services;

public interface LoggingService {
    void logInfo(String message, String className, String methodName, String userId, String requestUrl,
                 String requestMethod, Integer responseStatus);
    void logError(String message, String className, String methodName, String userId, String requestUrl,
                  String requestMethod, Integer responseStatus);
}