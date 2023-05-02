package br.com.emendes.adopetapi.integration.repository;

import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.ShelterRepository;
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
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("repository")
@DisplayName("Tests for ShelterRepository")
class ShelterRepositoryTest {

  @Autowired
  private ShelterRepository shelterRepository;

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("findByIdAndUserAndDeletedFalse must return Optional<Shelter> when found shelter")
  void findByIdAndUserAndDeletedFalse_MustReturnOptionalShelter_WhenFoundShelter() {
    User user = User.builder()
        .id(1L)
        .build();
    Optional<Shelter> actualShelterOptional = shelterRepository.findByIdAndUserAndDeletedFalse(1L, user);

    Assertions.assertThat(actualShelterOptional).isPresent();

    Shelter actualShelter = actualShelterOptional.get();

    Assertions.assertThat(actualShelter.getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("findByIdAndUserAndDeletedFalse must return Optional.empty when not found shelter with id = 100")
  void findByIdAndUserAndDeletedFalse_MustOptionalEmpty_WhenNotFoundShelterWithId100() {
    User user = User.builder()
        .id(1L)
        .build();
    Optional<Shelter> actualShelterOptional = shelterRepository.findByIdAndUserAndDeletedFalse(100L, user);

    Assertions.assertThat(actualShelterOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_DELETED_SHELTER_SQL_PATH})
  @DisplayName("findByIdAndUserAndDeletedFalse must return Optional.empty when shelter.deleted is true")
  void findByIdAndUserAndDeletedFalse_MustReturnOptionalEmpty_WhenShelterDeletedIsTrue() {
    User user = User.builder()
        .id(1L)
        .build();
    Optional<Shelter> actualShelterOptional = shelterRepository.findByIdAndUserAndDeletedFalse(1L, user);

    Optional<Shelter> actualShelterOptionalFindById = shelterRepository.findById(1L);

    Assertions.assertThat(actualShelterOptional).isNotPresent();
    Assertions.assertThat(actualShelterOptionalFindById).isPresent();
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("findByIdAndDeletedFalse must return Optional<Shelter> when found shelter")
  void findByIdAndDeletedFalse_MustReturnOptionalShelter_WhenFoundShelter() {
    Optional<Shelter> actualShelterOptional = shelterRepository.findByIdAndDeletedFalse(1L);

    Assertions.assertThat(actualShelterOptional).isPresent();

    Shelter actualShelter = actualShelterOptional.get();

    Assertions.assertThat(actualShelter.getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("findByIdAndDeletedFalse must return Optional.empty when not found shelter with id = 100")
  void findByIdAndDeletedFalse_MustOptionalEmpty_WhenNotFoundShelterWithId100() {
    Optional<Shelter> actualShelterOptional = shelterRepository.findByIdAndDeletedFalse(100L);

    Assertions.assertThat(actualShelterOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_DELETED_SHELTER_SQL_PATH})
  @DisplayName("findByIdAndDeletedFalse must return Optional.empty when shelter.deleted is true")
  void findByIdAndDeletedFalse_MustReturnOptionalEmpty_WhenShelterDeletedIsTrue() {
    Optional<Shelter> actualShelterOptional = shelterRepository.findByIdAndDeletedFalse(1L);

    Optional<Shelter> actualShelterOptionalFindById = shelterRepository.findById(1L);

    Assertions.assertThat(actualShelterOptional).isNotPresent();
    Assertions.assertThat(actualShelterOptionalFindById).isPresent();
  }

  @Test
  @Sql(scripts = {INSERT_MANY_DELETED_SHELTER_SQL_PATH})
  @DisplayName("findByDeletedFalse must return Page<Shelter> with size 2 when found only non deleted shelters")
  void findByDeletedFalse_MustReturnPageShelterWithSize2_WhenFoundOnlyNonDeletedShelters() {
    Page<Shelter> actualPageShelter = shelterRepository.findByDeletedFalse(PAGEABLE);

    long actualShelterCount = shelterRepository.count();

    Assertions.assertThat(actualPageShelter).hasSize(2);
    Assertions.assertThat(actualShelterCount).isEqualTo(4);
  }

}