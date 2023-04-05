package br.com.emendes.adopetapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TutorResponse {

  private Long id;
  private String name;
  private String email;

}
