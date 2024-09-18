package dev.mirrex.mappers;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.dto.response.GetNewsOutResponse;
import dev.mirrex.entities.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "author", ignore = true)
    News toNews(NewsCreateRequest dto);

    CreateNewsSuccessResponse toCreateNewsResponse(News entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "tags", ignore = true)
    void updateNews(NewsCreateRequest newsCreateRequest, @MappingTarget News news);

    @Mapping(target = "userId", source = "author.id")
    @Mapping(target = "username", source = "author.name")
    GetNewsOutResponse toGetNewsOutResponse(News news);
}
