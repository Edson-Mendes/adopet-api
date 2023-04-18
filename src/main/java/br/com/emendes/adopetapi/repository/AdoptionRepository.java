package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdoptionRepository extends JpaRepository<Adoption, Long> {

  Page<Adoption> findAllByPetShelter(Shelter shelter, Pageable pageable);

  Page<Adoption> findAllByGuardian(Guardian guardian, Pageable pageable);

}
