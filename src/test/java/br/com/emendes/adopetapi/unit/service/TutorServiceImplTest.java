package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

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

  private TutorResponse tutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
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