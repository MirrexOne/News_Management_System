package dev.mirrex.services;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.GetNewsOutResponse;
import dev.mirrex.dto.response.PageableResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;
import dev.mirrex.dto.response.baseResponse.CustomSuccessResponse;
import java.util.List;

public interface NewsService {

    CreateNewsSuccessResponse createNews(NewsCreateRequest newsCreateRequest);

    BaseSuccessResponse deleteNewsById(Long id);

    BaseSuccessResponse updateNewsById(Long id, NewsCreateRequest newsUpdate);

    CustomSuccessResponse<PageableResponse<List<GetNewsOutResponse>>> getNews(Integer page, Integer perPage);
}
