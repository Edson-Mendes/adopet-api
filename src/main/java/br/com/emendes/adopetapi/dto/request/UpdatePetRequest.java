package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record UpdatePetRequest(
    @NotBlank(message = "name must not be blank")
    @Size(min = 2, max = 100, message = "name must contain between {min} and {max} characters")
    String name,
    @NotBlank(message = "description must not be blank")
    @Size(max = 255, message = "description must contain max {max} characters")
    String description,
    @NotBlank(message = "age must not be blank")
    @Size(min = 1, max = 50, message = "age must contain between {min} and {max} characters")
    String age,
    @NotBlank(message = "image must not be blank")
    @URL(message = "image must be a well-formed url")
    @Size(max = 255, message = "image must contain max {max} characters")
    String image
) {

}
