package dev.mirrex.mappers;

import dev.mirrex.dto.request.NewsCreateRequest;
import dev.mirrex.dto.response.CreateNewsSuccessResponse;
import dev.mirrex.entities.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tags", ignore = true)
    News toEntity(NewsCreateRequest dto);

    CreateNewsSuccessResponse toDto(News entity);
}