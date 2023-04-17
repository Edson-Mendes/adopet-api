package br.com.emendes.adopetapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "t_adoption")
public class Adoption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  private Pet pet;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Guardian guardian;

}
