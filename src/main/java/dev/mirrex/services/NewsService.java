package dev.mirrex.services;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.GetNewsOutResponse;
import dev.mirrex.dto.response.PageableResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import java.util.List;
import java.util.UUID;

public interface NewsService {

    CreateNewsSuccessResponse createNews(NewsCreateRequest newsCreateRequest);

    BaseSuccessResponse deleteNewsById(Long id);

    BaseSuccessResponse updateNewsById(Long id, NewsCreateRequest newsUpdate);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(Integer page, Integer perPage);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getUserNews(UUID userId, Integer page, Integer perPage);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> findNews(
            String author, String keywords, Integer page, Integer perPage, List<String> tags);
}
