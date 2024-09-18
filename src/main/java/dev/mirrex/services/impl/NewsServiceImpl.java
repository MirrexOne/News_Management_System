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
import dev.mirrex.repositories.UserRepository;
import dev.mirrex.services.NewsService;
import dev.mirrex.services.TagService;
import dev.mirrex.services.UserService;
import dev.mirrex.util.ErrorCode;
import lombok.RequiredArgsConstructor;
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

    private final NewsRepository newsRepository;

    private final TagService tagService;

    private final UserRepository userRepository;

    private final UserService userService;

    private final NewsMapper newsMapper;

    @Override
    @Transactional
    public CreateNewsSuccessResponse createNews(NewsCreateRequest newsDto) {
        User currentUser = userService.getCurrentUser();

        News news = newsMapper.toNews(newsDto);
        news.setAuthor(currentUser);

        Set<Tag> tags = tagService.getOrCreateTags(newsDto.getTags());
        news.setTags(tags);

        News savedNews = newsRepository.save(news);
        return newsMapper.toCreateNewsResponse(savedNews);
    }

    @Override
    @Transactional
    public BaseSuccessResponse deleteNewsById(Long id) {
        User currentUser = userService.getCurrentUser();
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWS_NOT_FOUND));

        if (!userService.hasAccessToResource(news, currentUser)) {
            throw new CustomException(ErrorCode.NEWS_ACCESS_DENIED);
        }

        newsRepository.delete(news);
        return new BaseSuccessResponse();
    }

    @Override
    @Transactional
    public BaseSuccessResponse updateNewsById(Long id, NewsCreateRequest newsUpdate) {
        User currentUser = userService.getCurrentUser();
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NEWS_NOT_FOUND));

        if (!userService.hasAccessToResource(news, currentUser)) {
            throw new CustomException(ErrorCode.NEWS_ACCESS_DENIED);
        }

        newsMapper.updateNews(newsUpdate, news);

        Set<Tag> tags = tagService.getOrCreateTags(newsUpdate.getTags());
        news.setTags(tags);

        newsRepository.save(news);

        return new BaseSuccessResponse();
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("id").descending());
        Page<News> newsPage = newsRepository.findAll(pageable);

        List<GetNewsOutResponse> newsDtoList = newsPage.getContent().stream()
                .map(newsMapper::toGetNewsOutResponse)
                .collect(Collectors.toList());

        PageableResponse<List<GetNewsOutResponse>> pageableResponse = new PageableResponse<>(
                newsDtoList,
                newsPage.getTotalElements()
        );

        return new CustomSuccessResponse<>(pageableResponse);
    }

    @Override
    public CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNews(UUID userId, Integer page, Integer perPage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by("id").descending());
        Page<News> newsPage = newsRepository.findByAuthor(user, pageable);

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
