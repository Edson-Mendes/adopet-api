package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {
}
