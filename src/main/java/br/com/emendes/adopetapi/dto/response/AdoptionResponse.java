package br.com.emendes.adopetapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AdoptionResponse {

  private Long id;
  private Long petId;
  private Long guardianId;

}
