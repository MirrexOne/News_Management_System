package dev.mirrex.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUserDtoRequest {

    private Long id;

    private String name;

    private String email;

    private String avatar;

    private String role;

    private String token;
}
