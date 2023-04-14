package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateShelterRequest {

  @NotBlank(message = "name must not be blank")
  @Size(min = 2, max = 100, message = "name must contain between {min} and {max} characters")
  private String name;

  @NotBlank(message = "email must not be blank")
  @Size(max = 255, message = "email must contain max {max} characters")
  @Email(message = "must be a well formed email")
  private String email;

}
