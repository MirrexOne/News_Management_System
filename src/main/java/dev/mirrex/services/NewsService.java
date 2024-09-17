package dev.mirrex.services;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.baseResponse.BaseSuccessResponse;

public interface NewsService {

    CreateNewsSuccessResponse createNews(NewsCreateRequest newsCreateRequest);

    BaseSuccessResponse deleteNewsById(Long id);
}
