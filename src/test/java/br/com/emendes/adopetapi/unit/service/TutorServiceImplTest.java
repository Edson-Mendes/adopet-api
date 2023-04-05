package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
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
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;

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
      BDDMockito.when(tutorMapperMock.tutorRequestToTutor(any(TutorRequest.class)))
          .thenReturn(tutorWithoutId());
      BDDMockito.when(tutorRepositoryMock.save(any(Tutor.class)))
          .thenReturn(tutor());
      BDDMockito.when(tutorMapperMock.tutorToTutorResponse(any(Tutor.class)))
          .thenReturn(tutorResponse());

      TutorRequest tutorRequest = TutorRequest.builder()
          .name("Lorem Ipsum")
          .email("lorem@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      TutorResponse actualTutorResponse = tutorService.create(tutorRequest);

      BDDMockito.verify(tutorMapperMock).tutorRequestToTutor(any(TutorRequest.class));
      BDDMockito.verify(tutorMapperMock).tutorToTutorResponse(any(Tutor.class));
      BDDMockito.verify(tutorRepositoryMock).save(any(Tutor.class));

      Assertions.assertThat(actualTutorResponse).isNotNull();
      Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("lorem@email.com");
    }

    @Test
    @DisplayName("Create must throws PasswordsDoNotMatchException when passwords do not match")
    void create_MustThrowPasswordsDoNotMatchException_WhenPasswordsDoNotMatch() {
      TutorRequest tutorRequest = TutorRequest.builder()
          .password("123456789")
          .confirmPassword("12345678")
          .build();

      Assertions.assertThatExceptionOfType(PasswordsDoNotMatchException.class)
          .isThrownBy(() -> tutorService.create(tutorRequest))
          .withMessage("Passwords do not match");
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