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
@Table(name = "t_shelter")
public class Shelter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(length = 100, nullable = false)
  private String name;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @OneToMany(mappedBy = "shelter", cascade = {CascadeType.REMOVE})
  private Collection<Pet> pets;
  @OneToOne(cascade = CascadeType.REMOVE, optional = false)
  private User user;

}
