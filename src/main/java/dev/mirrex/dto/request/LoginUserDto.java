package dev.mirrex.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDto {

    private Long id;

    private String name;

    private String email;

    private String avatar;

    private String role;

    private String token;
}
