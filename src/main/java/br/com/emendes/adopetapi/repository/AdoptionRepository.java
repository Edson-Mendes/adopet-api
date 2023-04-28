package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

  Page<Adoption> findAllByPetShelter(Shelter shelter, Pageable pageable);

  Page<Adoption> findAllByGuardian(Guardian guardian, Pageable pageable);

  Optional<Adoption> findByIdAndPetShelter(Long id, Shelter shelter);

  Optional<Adoption> findByIdAndGuardian(Long id, Guardian guardian);

}
