package dev.mirrex.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {

    private String id;

    private String name;

    private String email;

    private String avatar;

    private String role;

    private String token;
}
