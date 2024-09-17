package dev.mirrex.dto.request;

import dev.mirrex.util.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NewsCreateRequest {

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_TITLE_SIZE)
    @NotBlank(message = ValidationConstants.NEWS_TITLE_NOT_NULL)
    private String title;

    @Size(min = 3, max = 160, message = ValidationConstants.NEWS_DESCRIPTION_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.NEWS_DESCRIPTION_HAS_TO_BE_PRESENT)
    private String description;

    @Size(min = 3, max = 130, message = ValidationConstants.NEWS_IMAGE_LENGTH)
    @NotBlank(message = ValidationConstants.NEWS_IMAGE_HAS_TO_BE_PRESENT)
    private String image;

    @NotEmpty(message = ValidationConstants.TAGS_NOT_VALID)
    private List<@NotBlank(message = ValidationConstants.TAGS_NOT_VALID) String> tags;
}
