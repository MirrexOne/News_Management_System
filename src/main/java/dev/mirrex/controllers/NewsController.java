package dev.mirrex.controllers;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.services.NewsService;
import dev.mirrex.util.ValidationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsSuccessResponse> createNews(
            @Valid @RequestBody NewsCreateRequest newsDto) {
        CreateNewsSuccessResponse response = newsService.createNews(newsDto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> deleteNewsById(
            @PathVariable @Min(value = 1, message = ValidationConstants.ID_MUST_BE_POSITIVE) Long id) {
        BaseSuccessResponse response = newsService.deleteNewsById(id);
        return ResponseEntity.ok().body(response);
    }
}
