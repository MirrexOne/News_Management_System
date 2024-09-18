package dev.mirrex.controllers;

import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/uploadFile")
    public ResponseEntity<CustomSuccessResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.uploadFile(file);
        return ResponseEntity.ok(new CustomSuccessResponse<>(fileUrl));
    }
}