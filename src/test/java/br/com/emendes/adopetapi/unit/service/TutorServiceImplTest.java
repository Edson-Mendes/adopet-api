package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.TutorNotFoundException;
import br.com.emendes.adopetapi.mapper.TutorMapper;
import br.com.emendes.adopetapi.model.entity.Tutor;
import br.com.emendes.adopetapi.repository.TutorRepository;
import br.com.emendes.adopetapi.service.impl.TutorServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for TutorServiceImpl")
class TutorServiceImplTest {

  @InjectMocks
  private TutorServiceImpl tutorService;
  @Mock
  private TutorMapper tutorMapperMock;
  @Mock
  private TutorRepository tutorRepositoryMock;

  @Nested
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("Create must return TutorResponse when create successfully")
    void create_MustReturnTutorResponse_WhenCreateSuccessfully() {
      BDDMockito.when(tutorMapperMock.tutorRequestToTutor(any(CreateTutorRequest.class)))
          .thenReturn(tutorWithoutId());
      BDDMockito.when(tutorRepositoryMock.save(any(Tutor.class)))
          .thenReturn(tutor());
      BDDMockito.when(tutorMapperMock.tutorToTutorResponse(any(Tutor.class)))
          .thenReturn(tutorResponse());

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .name("Lorem Ipsum")
          .email("lorem@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      TutorResponse actualTutorResponse = tutorService.create(createTutorRequest);

      BDDMockito.verify(tutorMapperMock).tutorRequestToTutor(any(CreateTutorRequest.class));
      BDDMockito.verify(tutorMapperMock).tutorToTutorResponse(any(Tutor.class));
      BDDMockito.verify(tutorRepositoryMock).save(any(Tutor.class));

      Assertions.assertThat(actualTutorResponse).isNotNull();
      Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("lorem@email.com");
    }

    @Test
    @DisplayName("Create must throw PasswordsDoNotMatchException when passwords do not match")
    void create_MustThrowPasswordsDoNotMatchException_WhenPasswordsDoNotMatch() {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .password("123456789")
          .confirmPassword("12345678")
          .build();

      Assertions.assertThatExceptionOfType(PasswordsDoNotMatchException.class)
          .isThrownBy(() -> tutorService.create(createTutorRequest))
          .withMessage("Passwords do not match");
    }

    @Test
    @DisplayName("Create must throw EmailAlreadyInUseException when already exists email in the database")
    void create_MustThrowEmailAlreadyInUseException_WhenAlreadyExistsEmailInTheDatabase() {
      BDDMockito.when(tutorMapperMock.tutorRequestToTutor(any(CreateTutorRequest.class)))
          .thenReturn(tutorWithoutId());
      BDDMockito.when(tutorRepositoryMock.save(any(Tutor.class)))
          .thenThrow(new DataIntegrityViolationException("unique_email constraint"));

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .name("Lorem Ipsum")
          .email("lorem@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      Assertions.assertThatExceptionOfType(EmailAlreadyInUseException.class)
          .isThrownBy(() -> tutorService.create(createTutorRequest))
          .withMessage("E-mail {lorem@email.com} is already in use");
    }

  }

  @Nested
  @DisplayName("Tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("Update must return TutorResponse when update successfully")
    void update_MustReturnTutorResponse_WhenUpdateSuccessfully() {
      BDDMockito.when(tutorRepositoryMock.findById(100L))
          .thenReturn(Optional.of(tutor()));
      BDDMockito.when(tutorRepositoryMock.save(any(Tutor.class)))
          .thenReturn(updatedTutor());
      BDDMockito.when(tutorMapperMock.tutorToTutorResponse(any(Tutor.class)))
          .thenReturn(updatedTutorResponse());

      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .name("Lorem Ipsum Dolor")
          .email("loremdolor@email.com")
          .build();

      TutorResponse actualTutorResponse = tutorService.update(100L, updateTutorRequest);

      BDDMockito.verify(tutorMapperMock).tutorToTutorResponse(any(Tutor.class));
      BDDMockito.verify(tutorRepositoryMock).save(any(Tutor.class));
      BDDMockito.verify(tutorRepositoryMock).findById(100L);

      Assertions.assertThat(actualTutorResponse).isNotNull();
      Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum Dolor");
      Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("loremdolor@email.com");
    }

    @Test
    @DisplayName("Update must throw EmailAlreadyInUseException when already exists email in the database")
    void update_MustThrowEmailAlreadyInUseException_WhenAlreadyExistsEmailInTheDatabase() {
      BDDMockito.when(tutorRepositoryMock.findById(100L))
          .thenReturn(Optional.of(tutor()));
      BDDMockito.when(tutorRepositoryMock.save(any(Tutor.class)))
          .thenThrow(new DataIntegrityViolationException("unique_email constraint"));

      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .name("Lorem Ipsum Dolor")
          .email("loremdolor@email.com")
          .build();

      Assertions.assertThatExceptionOfType(EmailAlreadyInUseException.class)
          .isThrownBy(() -> tutorService.update(100L, updateTutorRequest))
          .withMessage("E-mail {loremdolor@email.com} is already in use");
    }

    @Test
    @DisplayName("Update must throw TutorNotFoundException when tutor not found with given id")
    void update_MustThrowTutorNotFoundException_WhenTutorNotFoundWithGivenId() {
      BDDMockito.when(tutorRepositoryMock.findById(100L))
          .thenReturn(Optional.empty());

      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .name("Lorem Ipsum Dolor")
          .email("loremdolor@email.com")
          .build();

      Assertions.assertThatExceptionOfType(TutorNotFoundException.class)
          .isThrownBy(() -> tutorService.update(100L, updateTutorRequest))
          .withMessage("Tutor not found");
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("FindById must return TutorResponse when found successfully")
    void findById_MustReturnTutorResponse_WhenFoundSuccessfully() {
      BDDMockito.when(tutorRepositoryMock.findById(100L))
          .thenReturn(Optional.of(tutor()));
      BDDMockito.when(tutorMapperMock.tutorToTutorResponse(any(Tutor.class)))
          .thenReturn(tutorResponse());

      TutorResponse actualTutorResponse = tutorService.findById(100L);

      BDDMockito.verify(tutorRepositoryMock).findById(100L);
      BDDMockito.verify(tutorMapperMock).tutorToTutorResponse(any(Tutor.class));

      Assertions.assertThat(actualTutorResponse).isNotNull();
      Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("lorem@email.com");
    }

    @Test
    @DisplayName("FindById must throw TutorNotFoundException when tutor not found with given id")
    void findById_MustThrowTutorNotFoundException_WhenTutorNotFoundWithGivenId() {
      BDDMockito.when(tutorRepositoryMock.findById(100L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(TutorNotFoundException.class)
          .isThrownBy(() -> tutorService.findById(100L))
          .withMessage("Tutor not found");
    }

  }

  @Nested
  @DisplayName("Tests for fetchAll method")
  class FetchAllMethod {

    @Test
    @DisplayName("fetchAll must return Page<TutorResponse> when fetch successfully")
    void fetchAll_MustReturnPageTutorResponse_WhenFetchSuccessfully() {
      BDDMockito.when(tutorRepositoryMock.findAll(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(tutor()), PAGEABLE, 1));
      BDDMockito.when(tutorMapperMock.tutorToTutorResponse(any(Tutor.class)))
          .thenReturn(tutorResponse());

      Page<TutorResponse> actualTutorResponsePage = tutorService.fetchAll(PAGEABLE);

      BDDMockito.verify(tutorRepositoryMock).findAll(any(Pageable.class));
      BDDMockito.verify(tutorMapperMock).tutorToTutorResponse(any(Tutor.class));

      Assertions.assertThat(actualTutorResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

  @Nested
  @DisplayName("Tests for deleteById method")
  class DeleteByIdMethod {

    @Test
    @DisplayName("DeleteById must call TutorRepository#delete when delete tutor")
    void deleteById_MustCallTutorRepositoryDelete_WhenDeleteTutor() {
      BDDMockito.doNothing().when(tutorRepositoryMock).delete(any(Tutor.class));

      tutorService.deleteById(100L);

      BDDMockito.verify(tutorRepositoryMock).findById(100L);
      BDDMockito.verify(tutorRepositoryMock).delete(any(Tutor.class));
    }

    @Test
    @DisplayName("DeleteById must throw TutorNotFoundException when tutor do not exists with given id")
    void deleteById_MustThrowTutorNotFoundException_WhenTutorDoNotExistsWithGivenId() {
      BDDMockito.when(tutorRepositoryMock.findById(100L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(TutorNotFoundException.class)
          .isThrownBy(() -> tutorService.deleteById(100L))
          .withMessage("Tutor not found");
    }

  }

  private TutorResponse tutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .build();
  }

  private TutorResponse updatedTutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum Dolor")
        .email("loremdolor@email.com")
        .build();
  }

  private Tutor tutor() {
    return Tutor.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .deletedAt(null)
        .enabled(true)
        .build();
  }

  private Tutor updatedTutor() {
    return Tutor.builder()
        .id(100L)
        .name("Lorem Ipsum Dolor")
        .email("loremdolor@email.com")
        .password("1234567890")
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .deletedAt(null)
        .enabled(true)
        .build();
  }

  private Tutor tutorWithoutId() {
    return Tutor.builder()
        .id(null)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .createdAt(null)
        .deletedAt(null)
        .enabled(true)
        .build();
  }

}