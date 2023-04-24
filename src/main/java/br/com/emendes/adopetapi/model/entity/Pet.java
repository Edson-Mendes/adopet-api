package br.com.emendes.adopetapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
  @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private Collection<PetImage> images;
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Shelter shelter;
  @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
  private List<Adoption> adoptions;

}
