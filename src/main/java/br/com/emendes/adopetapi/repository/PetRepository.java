package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.Shelter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {

  Page<Pet> findByAdoptedFalseAndShelterDeletedFalse(Pageable pageable);

  Optional<Pet> findByIdAndShelter(Long id, Shelter shelter);

  Optional<Pet> findByIdAndShelterDeletedFalse(Long id);

  /**
   * Verifica se existe Pet que satisfaça as seguintes restrições:<br>
   * <li>Pet.id == {@code id} AND</li>
   * <li>Pet.adopted == {@code false} AND</li>
   * <li>Pet.shelter.deleted == {@code false}</li>
   * @param id do Pet
   * @return true se satisfazer as condições listadas acima,
   * false caso contrário.
   */
  boolean existsByIdAndAdoptedFalseAndShelterDeletedFalse(Long id);

}
