package dev.mirrex.services.impl;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.entities.News;
import dev.mirrex.entities.Tag;
import dev.mirrex.entities.User;
import dev.mirrex.mappers.NewsMapper;
import dev.mirrex.repositories.NewsRepository;
import dev.mirrex.services.NewsService;
import dev.mirrex.services.TagService;
import dev.mirrex.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final TagService tagService;

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
}
