package dev.mirrex.services.impl;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.entities.News;
import dev.mirrex.entities.Tag;
import dev.mirrex.mappers.NewsMapper;
import dev.mirrex.repositories.NewsRepository;
import dev.mirrex.repositories.TagRepository;
import dev.mirrex.services.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    private final TagRepository tagRepository;

    private final NewsMapper newsMapper;

    @Override
    @Transactional
    public CreateNewsSuccessResponse createNews(NewsCreateRequest newsDto) {
        News news = newsMapper.toEntity(newsDto);

        Set<Tag> tags = new HashSet<>();
        for (String tagName : newsDto.getTags()) {
            Tag tag = tagRepository.findByTitle(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setTitle(tagName);
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        news.setTags(tags);

        News savedNews = newsRepository.save(news);

        return newsMapper.toDto(savedNews);
    }
}