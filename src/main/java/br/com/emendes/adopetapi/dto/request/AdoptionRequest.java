package br.com.emendes.adopetapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdoptionRequest {

  @NotNull(message = "petId must not be null")
  @Positive(message = "petId must be greater than zero")
  private Long petId;

}
