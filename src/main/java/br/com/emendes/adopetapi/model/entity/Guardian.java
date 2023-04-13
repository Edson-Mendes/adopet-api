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
@Table(name = "t_guardian")
public class Guardian {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 100)
  private String name;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, optional = false)
  private User user;

}
