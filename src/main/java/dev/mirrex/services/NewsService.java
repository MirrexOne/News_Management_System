package dev.mirrex.services;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;

public interface NewsService {

    CreateNewsSuccessResponse createNews(NewsCreateRequest newsCreateRequest);
}
