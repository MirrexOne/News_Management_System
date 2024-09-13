package dev.mirrex.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicUserViewResponse {

    private String avatar;

    private String email;

    private Long id;

    private String name;

    private String role;
}
