package br.com.emendes.adopetapi.dto.request;

import br.com.emendes.adopetapi.validation.annotation.ValidStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateStatusRequest {

  @NotBlank(message = "status must not be blank")
  @ValidStatus
  private String status;

}
