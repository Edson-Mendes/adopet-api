package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.config.ModelMapperConfig;
import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.mapper.impl.TutorMapperImpl;
import br.com.emendes.adopetapi.model.entity.Tutor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Unit tests for TutorMapperImpl")
class TutorMapperImplTest {

  private TutorMapperImpl tutorMapper;

  @BeforeEach
  void setUp() {
    tutorMapper = new TutorMapperImpl(new ModelMapperConfig().modelMapper());
  }

  @Test
  @DisplayName("TutorRequestToTutor must return Tutor when map successfully")
  void tutorRequestToTutor_MustReturnTutor_WhenMapSuccessfully() {
    CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .confirmPassword("1234567890")
        .build();

    Tutor actualTutor = tutorMapper.tutorRequestToTutor(createTutorRequest);

    Assertions.assertThat(actualTutor).isNotNull();
    Assertions.assertThat(actualTutor.getId()).isNull();
    Assertions.assertThat(actualTutor.getName()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualTutor.getEmail()).isNotNull().isEqualTo("lorem@email.com");
    Assertions.assertThat(actualTutor.getPassword()).isNotNull().isEqualTo("1234567890");
    Assertions.assertThat(actualTutor.getCreatedAt()).isNull();
    Assertions.assertThat(actualTutor.getDeletedAt()).isNull();
    Assertions.assertThat(actualTutor.isEnabled()).isTrue();
  }

  @Test
  @DisplayName("TutorToTutorResponse must return TutorResponse when map successfully")
  void tutorToTutorResponse_MustReturnTutorResponse_WhenMapSuccessfully() {
    Tutor tutor = Tutor.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .deletedAt(null)
        .enabled(true)
        .build();

    TutorResponse actualTutorResponse = tutorMapper.tutorToTutorResponse(tutor);

    Assertions.assertThat(actualTutorResponse).isNotNull();
    Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
    Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("lorem@email.com");
  }

}