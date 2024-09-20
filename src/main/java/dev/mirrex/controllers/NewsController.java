package dev.mirrex.controllers;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.GetNewsOutResponse;
import dev.mirrex.dto.response.PageableResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.services.NewsService;
import dev.mirrex.util.Constants;
import dev.mirrex.util.ValidationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Validated
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<CreateNewsSuccessResponse> createNews(
            @Valid @RequestBody NewsCreateRequest newsDto) {
        logger.info("Received request to create news: {}", newsDto.getTitle());
        CreateNewsSuccessResponse response = newsService.createNews(newsDto);
        logger.info("News created successfully with ID: {}", response.getId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> deleteNewsById(
            @PathVariable @Min(value = 1, message = ValidationConstants.ID_MUST_BE_POSITIVE) Long id) {
        logger.info("Received request to delete news with ID: {}", id);
        BaseSuccessResponse response = newsService.deleteNewsById(id);
        logger.info("News with ID: {} deleted successfully", id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseSuccessResponse> updateNews(
            @PathVariable @Min(value = 1, message = ValidationConstants.ID_MUST_BE_POSITIVE) Long id,
            @Valid @RequestBody NewsCreateRequest newsUpdate) {
        logger.info("Received request to update news with ID: {}", id);
        BaseSuccessResponse response = newsService.updateNewsById(id, newsUpdate);
        logger.info("News with ID: {} updated successfully", id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getNews(
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage) {
        logger.info("Received request to get news. Page: {}, PerPage: {}", page, perPage);
        CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> response = newsService.getNews(page, perPage);
        logger.info("Returning news list. Total elements: {}", response.getData().getNumberOfElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> getUserNews(
            @PathVariable @Pattern(regexp = Constants.UUID_REGEX,
                    message = ValidationConstants.MAX_UPLOAD_SIZE_EXCEEDED) String userId,
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage) {

        UUID userUUID = UUID.fromString(userId);
        logger.info("Received request to get news for user: {}. Page: {}, PerPage: {}", userUUID, page, perPage);
        CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> response = newsService.getUserNews(userUUID, page, perPage);
        logger.info("Returning news list for user: {}. Total elements: {}", userUUID, response.getData().getNumberOfElements());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity<CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>>> findNews(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String keywords,
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PAGE_GREATER_OR_EQUAL_1) Integer page,
            @RequestParam @Min(value = 1, message = ValidationConstants.TASKS_PER_PAGE_GREATER_OR_EQUAL_1)
            @Max(value = 100, message = ValidationConstants.TASKS_PER_PAGE_LESS_OR_EQUAL_100) Integer perPage,
            @RequestParam(required = false) List<String> tags) {
        logger.info("Received request to find news. Author: {}, Keywords: {}, Page: {}, PerPage: {}, Tags: {}",
                author, keywords, page, perPage, tags);
        CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> response = newsService.findNews(author, keywords, page, perPage, tags);
        logger.info("Returning found news list. Total elements: {}", response.getData().getNumberOfElements());
        return ResponseEntity.ok(response);
    }
}