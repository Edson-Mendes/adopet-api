package br.com.emendes.adopetapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PetResponse {

  private Long id;
  private String name;
  private String description;
  private String age;
  private boolean adopted;
  private Long shelterId;

}
