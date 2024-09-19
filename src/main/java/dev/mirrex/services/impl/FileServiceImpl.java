package dev.mirrex.services.impl;

import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.services.FileService;
import dev.mirrex.util.ErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    private static final AtomicLong fileCounter = new AtomicLong(0);

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFileName = String.format("%020d", fileCounter.incrementAndGet()) + fileExtension;
            Path path = Paths.get(uploadDir + newFileName);
            Files.write(path, file.getBytes());
            return baseUrl + "/file/" + newFileName;
        } catch (IOException ex) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public Resource getFile(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new CustomException(ErrorCode.EXCEPTION_HANDLER_NOT_PROVIDED);
            }
        } catch (MalformedURLException ex) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }
}
