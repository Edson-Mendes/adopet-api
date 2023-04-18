package br.com.emendes.adopetapi.dto.response;

import br.com.emendes.adopetapi.model.AdoptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdoptionResponse {

  private Long id;
  private Long petId;
  private Long guardianId;
  private AdoptionStatus status;
  private LocalDateTime date;

}
