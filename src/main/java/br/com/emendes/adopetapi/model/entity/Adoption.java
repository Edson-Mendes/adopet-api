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
@Table(name = "t_adoption")
public class Adoption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private LocalDateTime date;
  @OneToOne(optional = false, fetch = FetchType.LAZY)
  private Pet pet;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Guardian guardian;

}
