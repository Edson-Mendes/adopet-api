package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.mapper.impl.TutorMapperImpl;
import br.com.emendes.adopetapi.model.entity.Tutor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for TutorMapperImpl")
class TutorMapperImplTest {

  @InjectMocks
  private TutorMapperImpl tutorMapper;
  @Spy
  private ModelMapper modelMapper;

  @Nested
  @DisplayName("Tests for tutorRequestToTutor method")
  class TutorRequestToTutorMethod {

    @Test
    @DisplayName("TutorRequestToTutor must return Tutor when map successfully")
    void tutorRequestToTutor_MustReturnTutor_WhenMapSuccessfully() {
      TutorRequest tutorRequest = TutorRequest.builder()
          .name("Lorem Ipsum")
          .email("lorem@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      Tutor actualTutor = tutorMapper.tutorRequestToTutor(tutorRequest);

      Assertions.assertThat(actualTutor).isNotNull();
      Assertions.assertThat(actualTutor.getId()).isNull();
      Assertions.assertThat(actualTutor.getName()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualTutor.getEmail()).isNotNull().isEqualTo("lorem@email.com");
      Assertions.assertThat(actualTutor.getPassword()).isNotNull().isEqualTo("1234567890");
      Assertions.assertThat(actualTutor.getCreatedAt()).isNull();
      Assertions.assertThat(actualTutor.getDeletedAt()).isNull();
      Assertions.assertThat(actualTutor.getEnabled()).isNull();
    }

  }

}