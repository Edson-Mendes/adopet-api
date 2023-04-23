package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateShelterRequest(
    @NotBlank(message = "name must not be blank")
    @Size(min = 2, max = 100, message = "name must contain between {min} and {max} characters")
    String name,
    @NotBlank(message = "email must not be blank")
    @Size(max = 255, message = "email must contain max {max} characters")
    @Email(message = "must be a well formed email")
    String email) {

}
