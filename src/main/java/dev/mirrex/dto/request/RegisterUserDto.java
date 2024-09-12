package dev.mirrex.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {

    @NotBlank(message = "Name should be greater than 3 and less or equals 25 symbols")
    @Size(min = 3, max = 25)
    private String name;

    @NotBlank
    @Email(message = "Email should be greater than 3 and less or equals 100 symbols")
    @Size(min = 3, max = 100)
    private String email;

    @NotBlank(message = "Password should not be blank")
    private String password;

    private String avatar;

    @NotBlank(message = "Role should be greater than 3 and less or equals 25 symbols")
    @Size(min = 3, max = 25)
    private String role;
}
