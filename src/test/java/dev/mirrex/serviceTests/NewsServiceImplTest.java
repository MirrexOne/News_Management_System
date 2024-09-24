package dev.mirrex.serviceTests;

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
import dev.mirrex.services.TagService;
import dev.mirrex.services.UserService;
import dev.mirrex.services.impl.NewsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private TagService tagService;
    @Mock
    private UserService userService;
    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNews_Success() {
        NewsCreateRequest request = new NewsCreateRequest();
        User currentUser = new User();
        News news = new News();
        Set<Tag> tags = new HashSet<>();
        CreateNewsSuccessResponse expectedResponse = new CreateNewsSuccessResponse();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(newsMapper.toNews(any(NewsCreateRequest.class))).thenReturn(news);
        when(tagService.getOrCreateTags(anyList())).thenReturn(tags);
        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(newsMapper.toCreateNewsResponse(any(News.class))).thenReturn(expectedResponse);

        CreateNewsSuccessResponse result = newsService.createNews(request);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        verify(newsRepository).save(any(News.class));
    }

    @Test
    void deleteNewsById_Success() {
        Long newsId = 1L;
        User currentUser = new User();
        News news = new News();
        news.setAuthor(currentUser);

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(newsRepository.findById(anyLong())).thenReturn(Optional.of(news));
        when(userService.hasAccessToResource(any(News.class), any(User.class))).thenReturn(true);

        BaseSuccessResponse result = newsService.deleteNewsById(newsId);

        assertNotNull(result);
        verify(newsRepository).delete(news);
    }

    @Test
    void deleteNewsById_NewsNotFound() {
        Long newsId = 1L;
        when(newsRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> newsService.deleteNewsById(newsId));
    }

    @Test
    void updateNewsById_Success() {
        Long newsId = 1L;
        NewsCreateRequest request = new NewsCreateRequest();
        User currentUser = new User();
        News news = new News();
        Set<Tag> tags = new HashSet<>();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(newsRepository.findById(anyLong())).thenReturn(Optional.of(news));
        when(userService.hasAccessToResource(any(News.class), any(User.class))).thenReturn(true);
        when(tagService.getOrCreateTags(anyList())).thenReturn(tags);

        BaseSuccessResponse result = newsService.updateNewsById(newsId, request);

        assertNotNull(result);
        verify(newsMapper).updateNews(request, news);
        verify(newsRepository).save(news);
    }

    @Test
    void getNews_Success() {
        int page = 1;
        int perPage = 10;
        List<News> newsList = Collections.singletonList(new News());
        Page<News> newsPage = new PageImpl<>(newsList);

        when(newsRepository.findAll(any(Pageable.class))).thenReturn(newsPage);
        when(newsMapper.toGetNewsOutResponse(any(News.class))).thenReturn(new GetNewsOutResponse());

        CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> result = newsService.getNews(page, perPage);

        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getContent().size());
    }
}
