package dev.mirrex.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class LoginUserResponse {

    private UUID id;

    private String name;

    private String email;

    private String avatar;

    private String role;

    private String token;
}
