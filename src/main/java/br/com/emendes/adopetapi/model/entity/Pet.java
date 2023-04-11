package br.com.emendes.adopetapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "t_pet")
public class Pet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 100, nullable = false)
  private String name;
  @Column(nullable = false)
  private String description;
  @Column(nullable = false, length = 50)
  private String age;
  @Column(nullable = false)
  private boolean adopted;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @ManyToOne(optional = false)
  private Shelter shelter;

}
