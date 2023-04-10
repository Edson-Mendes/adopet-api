package br.com.emendes.adopetapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "t_animal_shelter")
public class AnimalShelter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 100, nullable = false)
  private String name;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @OneToMany(mappedBy = "animalShelter", cascade = {CascadeType.REMOVE})
  private Collection<Pet> pets;

}
