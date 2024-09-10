package dev.mirrex.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

    @NotBlank
    @Size(min = 3, max = 25)
    private String name;

    @NotBlank
    @Email
    @Size(min = 3, max = 100)
    private String email;

    @NotBlank
    private String password;

    private String avatar;

    @NotBlank
    @Size(min = 3, max = 25)
    private String role;
}
