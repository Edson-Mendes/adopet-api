package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateShelterRequest(
    @NotBlank(message = "name must not be blank")
    @Size(min = 2, max = 100, message = "name must contain between {min} and {max} characters")
    String name,

    @NotBlank(message = "email must not be blank")
    @Size(max = 255, message = "email must contain max {max} characters")
    @Email(message = "must be a well formed email")
    String email,

    @NotBlank(message = "password must not be blank")
    @Size(min = 8, max = 30, message = "password must contain between {min} and {max} characters")
    String password,

    @NotBlank(message = "confirmPassword must not be blank")
    @Size(min = 8, max = 30, message = "confirmPassword must contain between {min} and {max} characters")
    String confirmPassword) {

}
