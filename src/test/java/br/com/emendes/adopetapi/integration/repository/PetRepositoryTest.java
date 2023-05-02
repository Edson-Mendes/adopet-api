package br.com.emendes.adopetapi.integration.repository;

import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.repository.PetRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_MANY_SHELTERS_AND_MANY_PETS_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_PET_SHELTER_SQL_PATH;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("repository")
@DisplayName("Tests for PetRepository")
class PetRepositoryTest {

  @Autowired
  private PetRepository petRepository;

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_SQL_PATH})
  @DisplayName("findByAdoptedFalseAndShelterDeletedFalse must return Page<Pet> when found pets")
  void findByAdoptedFalseAndShelterDeletedFalse_MustReturnPagePet_WhenFoundPets() {
    Page<Pet> actualPetPage = petRepository.findByAdoptedFalseAndShelterDeletedFalse(PAGEABLE);

    Assertions.assertThat(actualPetPage).hasSize(1);
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_AND_MANY_PETS_SQL_PATH})
  @DisplayName("findByAdoptedFalseAndShelterDeletedFalse must return page with 2 pets when found successfully")
  void findByAdoptedFalseAndShelterDeletedFalse_MustReturnPageWith2Pets_WhenFoundSuccessfully() {
    Page<Pet> actualPetPage = petRepository.findByAdoptedFalseAndShelterDeletedFalse(PAGEABLE);

    Assertions.assertThat(actualPetPage).hasSize(2);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_SQL_PATH})
  @DisplayName("existsByIdAndAdoptedFalseAndShelterDeletedFalse must return true when exists pet")
  void existsByIdAndAdoptedFalseAndShelterDeletedFalse_MustReturnTrue_WhenExistsPet() {
    boolean actualExists = petRepository.existsByIdAndAdoptedFalseAndShelterDeletedFalse(1L);

    Assertions.assertThat(actualExists).isTrue();
  }

  @Test
  @DisplayName("existsByIdAndAdoptedFalseAndShelterDeletedFalse must return false when not found pet with id = 100")
  void existsByIdAndAdoptedFalseAndShelterDeletedFalse_MustReturnFalse_WhenNotFoundPetWithId100() {
    boolean actualExists = petRepository.existsByIdAndAdoptedFalseAndShelterDeletedFalse(100L);
    Optional<Pet> actualPetOptional = petRepository.findById(100L);

    Assertions.assertThat(actualExists).isFalse();
    Assertions.assertThat(actualPetOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_AND_MANY_PETS_SQL_PATH})
  @DisplayName("existsByIdAndAdoptedFalseAndShelterDeletedFalse must return false when pet is adopted")
  void existsByIdAndAdoptedFalseAndShelterDeletedFalse_MustReturnFalse_WhenPetIsAdopted() {
    boolean actualExists = petRepository.existsByIdAndAdoptedFalseAndShelterDeletedFalse(3L);

    Optional<Pet> actualPetOptional = petRepository.findById(3L);

    Assertions.assertThat(actualExists).isFalse();
    Assertions.assertThat(actualPetOptional).isPresent();

    Pet actualPet = actualPetOptional.get();

    Assertions.assertThat(actualPet.isAdopted()).isTrue();
    Assertions.assertThat(actualPet.getShelter()).isNotNull();
    Assertions.assertThat(actualPet.getShelter().isDeleted()).isFalse();
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_AND_MANY_PETS_SQL_PATH})
  @DisplayName("existsByIdAndAdoptedFalseAndShelterDeletedFalse must return false when pet belongs to a deleted shelter")
  void existsByIdAndAdoptedFalseAndShelterDeletedFalse_MustReturnFalse_WhenPetBelongsToADeletedShelter() {
    boolean actualExists = petRepository.existsByIdAndAdoptedFalseAndShelterDeletedFalse(5L);
    Optional<Pet> actualPetOptional = petRepository.findById(5L);

    Assertions.assertThat(actualExists).isFalse();
    Assertions.assertThat(actualPetOptional).isPresent();

    Pet actualPet = actualPetOptional.get();

    Assertions.assertThat(actualPet.isAdopted()).isFalse();
    Assertions.assertThat(actualPet.getShelter()).isNotNull();
    Assertions.assertThat(actualPet.getShelter().isDeleted()).isTrue();
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_SQL_PATH})
  @DisplayName("findByIdAndShelterDeletedFalse must return Optional<Pet> when found pet")
  void findByIdAndShelterDeletedFalse_MustReturnOptionalPet_WhenFoundPet() {
    Optional<Pet> actualPetOptional = petRepository.findByIdAndShelterDeletedFalse(1L);

    Assertions.assertThat(actualPetOptional).isPresent();

    Pet actualPet = actualPetOptional.get();

    Assertions.assertThat(actualPet.getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("findByIdAndShelterDeletedFalse must return Optional.empty when not found pet with id = 100")
  void findByIdAndShelterDeletedFalse_MustOptionalEmpty_WhenNotFoundPetWithId100() {
    Optional<Pet> actualPetOptional = petRepository.findByIdAndShelterDeletedFalse(100L);

    Assertions.assertThat(actualPetOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_AND_MANY_PETS_SQL_PATH})
  @DisplayName("findByIdAndShelterDeletedFalse must return Optional.empty when pet belongs to a deleted shelter")
  void findByIdAndShelterDeletedFalse_MustReturnOptionalEmpty_WhenPetBelongsToADeletedShelter() {
    Optional<Pet> actualPetOptional = petRepository.findByIdAndShelterDeletedFalse(5L);

    Optional<Pet> actualPetOptionalFindById = petRepository.findById(5L);

    Assertions.assertThat(actualPetOptional).isNotPresent();
    Assertions.assertThat(actualPetOptionalFindById).isPresent();

    Pet actualPet = actualPetOptionalFindById.get();

    Assertions.assertThat(actualPet.getId()).isEqualTo(5L);
    Assertions.assertThat(actualPet.getShelter()).isNotNull();
    Assertions.assertThat(actualPet.getShelter().isDeleted()).isTrue();
  }

}
