package dev.mirrex.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class LoginUserRequest {

    private UUID id;

    private String name;

    private String email;

    private String avatar;

    private String role;

    private String token;
}
