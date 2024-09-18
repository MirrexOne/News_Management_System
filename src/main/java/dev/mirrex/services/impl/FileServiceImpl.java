package dev.mirrex.services.impl;

import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.services.FileService;
import dev.mirrex.util.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.write(path, file.getBytes());
            return fileName;
        } catch (IOException ex) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }
}
