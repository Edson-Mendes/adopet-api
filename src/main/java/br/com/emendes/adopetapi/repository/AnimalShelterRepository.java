package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.AnimalShelter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalShelterRepository extends JpaRepository<AnimalShelter, Long> {
}
