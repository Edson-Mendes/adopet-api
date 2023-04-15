package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationRequest {

  @NotBlank(message = "email must not be blank")
  @Email(message = "must be a well formed email")
  private String email;
  @NotBlank(message = "password must not be blank")
  private String password;

}
