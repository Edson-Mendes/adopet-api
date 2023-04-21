package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

  Page<Pet> findByAdoptedFalse(Pageable pageable);

  Optional<Pet> findByIdAndShelter(Long id, Shelter shelter);
}
