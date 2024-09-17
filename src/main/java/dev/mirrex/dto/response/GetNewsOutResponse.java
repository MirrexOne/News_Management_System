package dev.mirrex.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GetNewsOutResponse {

    private Long id;

    private String title;

    private String description;

    private String image;

    private List<TagResponse> tags;

    private UUID userId;

    private String username;
}