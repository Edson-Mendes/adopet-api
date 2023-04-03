package br.com.emendes.adopetapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_tutor")
public class Tutor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 100)
  private String name;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false, unique = true)
  private String email;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private LocalDateTime deletedAt;
  private boolean enabled;

}
