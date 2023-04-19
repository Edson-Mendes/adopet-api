package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

  Page<Pet> findByAdoptedFalse(Pageable pageable);

}
