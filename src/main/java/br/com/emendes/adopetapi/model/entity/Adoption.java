package br.com.emendes.adopetapi.model.entity;

import br.com.emendes.adopetapi.model.AdoptionStatus;
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
  @Column(nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private AdoptionStatus status;
  @OneToOne(optional = false, fetch = FetchType.EAGER)
  private Pet pet;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Guardian guardian;

}
