package dev.mirrex.controllers;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.GetNewsOutResponse;
import dev.mirrex.dto.response.PageableResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.services.NewsService;
import dev.mirrex.util.ValidationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsSuccessResponse> createNews(
            @Valid @RequestBody NewsCreateRequest newsDto) {
        return ResponseEntity.ok().body(newsService.createNews(newsDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> deleteNewsById(
            @PathVariable @Min(value = 1, message = ValidationConstants.ID_MUST_BE_POSITIVE) Long id) {
        return ResponseEntity.ok().body(newsService.deleteNewsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> updateNews(
            @PathVariable @Min(value = 1, message = ValidationConstants.ID_MUST_BE_POSITIVE) Long id,
            @Valid @RequestBody NewsCreateRequest newsUpdate) {
        return ResponseEntity.ok().body(newsService.updateNewsById(id, newsUpdate));
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getNews(
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage) {
        return ResponseEntity.ok(newsService.getNews(page, perPage));
    }
}
