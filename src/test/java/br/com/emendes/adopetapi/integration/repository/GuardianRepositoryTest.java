package br.com.emendes.adopetapi.integration.repository;

import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.GuardianRepository;
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
@DisplayName("Tests for GuardianRepository")
class GuardianRepositoryTest {

  @Autowired
  private GuardianRepository guardianRepository;

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("findByIdAndUserAndDeletedFalse must return Optional<Guardian> when found guardian")
  void findByIdAndUserAndDeletedFalse_MustReturnOptionalGuardian_WhenFoundGuardian() {
    User user = User.builder()
        .id(1L)
        .build();
    Optional<Guardian> actualGuardianOptional = guardianRepository.findByIdAndUserAndDeletedFalse(1L, user);

    Assertions.assertThat(actualGuardianOptional).isPresent();

    Guardian actualGuardian = actualGuardianOptional.get();

    Assertions.assertThat(actualGuardian.getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("findByIdAndUserAndDeletedFalse must return Optional.empty when not found guardian with id = 100")
  void findByIdAndUserAndDeletedFalse_MustOptionalEmpty_WhenNotFoundGuardianWithId100() {
    User user = User.builder()
        .id(1L)
        .build();
    Optional<Guardian> actualGuardianOptional = guardianRepository.findByIdAndUserAndDeletedFalse(100L, user);

    Assertions.assertThat(actualGuardianOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_DELETED_GUARDIAN_SQL_PATH})
  @DisplayName("findByIdAndUserAndDeletedFalse must return Optional.empty when guardian.deleted is true")
  void findByIdAndUserAndDeletedFalse_MustReturnOptionalEmpty_WhenGuardianDeletedIsTrue() {
    User user = User.builder()
        .id(1L)
        .build();
    Optional<Guardian> actualGuardianOptional = guardianRepository.findByIdAndUserAndDeletedFalse(1L, user);

    Optional<Guardian> actualGuardianOptionalFindById = guardianRepository.findById(1L);

    Assertions.assertThat(actualGuardianOptional).isNotPresent();
    Assertions.assertThat(actualGuardianOptionalFindById).isPresent();
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("findByIdAndDeletedFalse must return Optional<Guardian> when found guardian")
  void findByIdAndDeletedFalse_MustReturnOptionalGuardian_WhenFoundGuardian() {
    Optional<Guardian> actualGuardianOptional = guardianRepository.findByIdAndDeletedFalse(1L);

    Assertions.assertThat(actualGuardianOptional).isPresent();

    Guardian actualGuardian = actualGuardianOptional.get();

    Assertions.assertThat(actualGuardian.getId()).isEqualTo(1L);
  }

  @Test
  @DisplayName("findByIdAndDeletedFalse must return Optional.empty when not found guardian with id = 100")
  void findByIdAndDeletedFalse_MustOptionalEmpty_WhenNotFoundGuardianWithId100() {
    Optional<Guardian> actualGuardianOptional = guardianRepository.findByIdAndDeletedFalse(100L);

    Assertions.assertThat(actualGuardianOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_DELETED_GUARDIAN_SQL_PATH})
  @DisplayName("findByIdAndDeletedFalse must return Optional.empty when guardian.deleted is true")
  void findByIdAndDeletedFalse_MustReturnOptionalEmpty_WhenGuardianDeletedIsTrue() {
    Optional<Guardian> actualGuardianOptional = guardianRepository.findByIdAndDeletedFalse(1L);

    Optional<Guardian> actualGuardianOptionalFindById = guardianRepository.findById(1L);

    Assertions.assertThat(actualGuardianOptional).isNotPresent();
    Assertions.assertThat(actualGuardianOptionalFindById).isPresent();
  }

  @Test
  @Sql(scripts = {INSERT_MANY_DELETED_GUARDIAN_SQL_PATH})
  @DisplayName("findByDeletedFalse must return Page<Guardian> with size 2 when found only non deleted guardians")
  void findByDeletedFalse_MustReturnPageGuardianWithSize2_WhenFoundOnlyNonDeletedGuardians() {
    Page<Guardian> actualPageGuardian = guardianRepository.findByDeletedFalse(PAGEABLE);

    long actualGuardianCount = guardianRepository.count();

    Assertions.assertThat(actualPageGuardian).hasSize(2);
    Assertions.assertThat(actualGuardianCount).isEqualTo(4);
  }

}