package dev.mirrex.serviceTests;

import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.services.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileServiceImplTest {

    @InjectMocks
    private FileServiceImpl fileService;

    @Mock
    private MultipartFile multipartFile;

    private final String uploadDir = "test-upload-dir";

    private final String filePrefix = "test-prefix-";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(fileService, "uploadDir", uploadDir);
        ReflectionTestUtils.setField(fileService, "FILE_PREFIX", filePrefix);
    }

    @Test
    void init_Success() {
        fileService.init();
        assertTrue(Files.exists(Paths.get(uploadDir)));
    }

    @Test
    void uploadFile_Success() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getBytes()).thenReturn("test content".getBytes());

        String result = fileService.uploadFile(multipartFile);

        assertTrue(result.startsWith(filePrefix));
        assertTrue(Files.exists(Paths.get(uploadDir, result.substring(filePrefix.length()))));
    }

    @Test
    void uploadFile_IOException() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");
        when(multipartFile.getBytes()).thenThrow(new IOException());

        assertThrows(CustomException.class, () -> fileService.uploadFile(multipartFile));
    }

    @Test
    void getFile_NotFound() {
        String fileName = "nonexistent.txt";

        assertThrows(CustomException.class, () -> fileService.getFile(fileName));
    }
}
