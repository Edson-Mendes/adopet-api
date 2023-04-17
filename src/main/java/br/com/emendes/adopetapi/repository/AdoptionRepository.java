package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {
}
