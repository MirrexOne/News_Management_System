package dev.mirrex.serviceTests;

import dev.mirrex.entities.Log;
import dev.mirrex.repositories.LogRepository;
import dev.mirrex.services.impl.LoggingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static dev.mirrex.util.Constants.ERROR;
import static dev.mirrex.util.Constants.INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoggingServiceImplTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LoggingServiceImpl loggingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logInfo_Success() {
        String message = "Info message";
        String className = "TestClass";
        String methodName = "testMethod";
        String userId = "user123";
        String requestUrl = "/api/test";
        String requestMethod = "GET";
        Integer responseStatus = 200;

        loggingService.logInfo(message, className, methodName, userId, requestUrl, requestMethod, responseStatus);

        ArgumentCaptor<Log> logCaptor = ArgumentCaptor.forClass(Log.class);
        verify(logRepository).save(logCaptor.capture());

        Log capturedLog = logCaptor.getValue();
        assertEquals(INFO, capturedLog.getLevel());
        assertEquals(message, capturedLog.getMessage());
        assertEquals(className, capturedLog.getClassName());
        assertEquals(methodName, capturedLog.getMethodName());
        assertEquals(userId, capturedLog.getUserId());
        assertEquals(requestUrl, capturedLog.getRequestUrl());
        assertEquals(requestMethod, capturedLog.getRequestMethod());
        assertEquals(responseStatus, capturedLog.getResponseStatus());
    }

    @Test
    void logError_Success() {
        String message = "Error message";
        String className = "TestClass";
        String methodName = "testMethod";
        String userId = "user123";
        String requestUrl = "/api/test";
        String requestMethod = "POST";
        Integer responseStatus = 500;

        loggingService.logError(message, className, methodName, userId, requestUrl, requestMethod, responseStatus);

        ArgumentCaptor<Log> logCaptor = ArgumentCaptor.forClass(Log.class);
        verify(logRepository).save(logCaptor.capture());

        Log capturedLog = logCaptor.getValue();
        assertEquals(ERROR, capturedLog.getLevel());
        assertEquals(message, capturedLog.getMessage());
        assertEquals(className, capturedLog.getClassName());
        assertEquals(methodName, capturedLog.getMethodName());
        assertEquals(userId, capturedLog.getUserId());
        assertEquals(requestUrl, capturedLog.getRequestUrl());
        assertEquals(requestMethod, capturedLog.getRequestMethod());
        assertEquals(responseStatus, capturedLog.getResponseStatus());
    }
}
