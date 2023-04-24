package br.com.emendes.adopetapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "t_pet_images")
public class PetImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String url;
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  private Pet pet;

}
