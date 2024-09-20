package dev.mirrex.services.impl;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.GetNewsOutResponse;
import dev.mirrex.dto.response.PageableResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import dev.mirrex.entities.News;
import dev.mirrex.entities.Tag;
import dev.mirrex.entities.User;
import dev.mirrex.exceptionHandlers.CustomException;
import dev.mirrex.mappers.NewsMapper;
import dev.mirrex.repositories.NewsRepository;
import dev.mirrex.services.NewsService;
import dev.mirrex.services.TagService;
import dev.mirrex.services.UserService;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;

    private final TagService tagService;

    private final UserService userService;

    private final NewsMapper newsMapper;

    @Override
    @Transactional
    public CreateNewsSuccessResponse createNews(NewsCreateRequest newsDto) {
        logger.info("Creating new news with title: {}", newsDto.getTitle());
        User currentUser = userService.getCurrentUser();

        News news = newsMapper.toNews(newsDto);
        news.setAuthor(currentUser);

        Set<Tag> tags = tagService.getOrCreateTags(newsDto.getTags());
        news.setTags(tags);

        News savedNews = newsRepository.save(news);
        logger.info("News created successfully with ID: {}", savedNews.getId());
        return newsMapper.toCreateNewsResponse(savedNews);
    }

    @Override
    @Transactional
    public BaseSuccessResponse deleteNewsById(Long id) {
        logger.info("Attempting to delete news with ID: {}", id);
        User currentUser = userService.getCurrentUser();
        News news = newsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("News not found with ID: {}", id);
                    return new CustomException(ErrorCode.NEWS_NOT_FOUND);
                });

        if (!userService.hasAccessToResource(news, currentUser)) {
            logger.warn("Access denied for user {} to delete news with ID: {}", currentUser.getId(), id);
            throw new CustomException(ErrorCode.NEWS_ACCESS_DENIED);
        }

        newsRepository.delete(news);
        logger.info("News with ID: {} deleted successfully", id);
        return new BaseSuccessResponse();
    }

    @Override
    @Transactional
    public BaseSuccessResponse updateNewsById(Long id, NewsCreateRequest newsUpdate) {
        logger.info("Attempting to update news with ID: {}", id);
        User currentUser = userService.getCurrentUser();
        News news = newsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("News not found with ID: {}", id);
                    return new CustomException(ErrorCode.NEWS_NOT_FOUND);
                });

        if (!userService.hasAccessToResource(news, currentUser)) {
            logger.warn("Access denied for user {} to update news with ID: {}", currentUser.getId(), id);
            throw new CustomException(ErrorCode.NEWS_ACCESS_DENIED);
        }

        newsMapper.updateNews(newsUpdate, news);

        Set<Tag> tags = tagService.getOrCreateTags(newsUpdate.getTags());
        news.setTags(tags);

        newsRepository.save(news);
        logger.info("News with ID: {} updated successfully", id);

        return new BaseSuccessResponse();
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(Integer page, Integer perPage) {
        logger.info("Fetching news page: {}, size: {}", page, perPage);
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("id").descending());
        Page<News> newsPage = newsRepository.findAll(pageable);
        logger.info("Found {} news items", newsPage.getTotalElements());
        return getPageableResponse(newsPage);
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNews(UUID userId, Integer page, Integer perPage) {
        logger.info("Fetching news for user: {}, page: {}, size: {}", userId, page, perPage);
        User user = userService.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", userId);
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("id").descending());
        Page<News> newsPage = newsRepository.findByAuthor(user, pageable);
        logger.info("Found {} news items for user: {}", newsPage.getTotalElements(), userId);
        return getPageableResponse(newsPage);
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> findNews(String author, String keywords, Integer page, Integer perPage, List<String> tags) {
        logger.info("Searching news with author: {}, keywords: {}, tags: {}, page: {}, size: {}", author, keywords, tags, page, perPage);
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("id").descending());
        Page<News> newsPage = newsRepository.findNewsByFilters(author, keywords, tags, pageable);
        logger.info("Found {} news items matching the criteria", newsPage.getTotalElements());
        return getPageableResponse(newsPage);
    }

    private CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getPageableResponse(Page<News> newsPage) {
        List<GetNewsOutResponse> newsList = newsPage.getContent().stream()
                .map(newsMapper::toGetNewsOutResponse)
                .collect(Collectors.toList());

        PageableResponse<List<GetNewsOutResponse>> pageableResponse = new PageableResponse<>(
                newsList,
                newsPage.getTotalElements()
        );

        return new CustomSuccessResponse<>(pageableResponse);
    }
}
