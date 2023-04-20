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
@Table(name = "t_guardian")
public class Guardian {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 100)
  private String name;
  @Column(nullable = false)
  private LocalDateTime createdAt;
  @Column(nullable = false)
  private boolean deleted;
  @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, optional = false, fetch = FetchType.LAZY)
  private User user;
  @OneToMany(mappedBy = "guardian")
  private Collection<Adoption> adoptions;

}
