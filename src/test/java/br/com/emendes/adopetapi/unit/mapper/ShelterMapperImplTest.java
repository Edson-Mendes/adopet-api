package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.mapper.impl.ShelterMapperImpl;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Unit tests for ShelterMapperImpl")
class ShelterMapperImplTest {

  private ShelterMapperImpl shelterMapper;

  @BeforeEach
  void setUp() {
    shelterMapper = new ShelterMapperImpl();
  }

  @Test
  @DisplayName("CreateShelterRequestToShelter must return Shelter when map successfully")
  void createShelterRequestToShelter_MustReturnShelter_WhenMapSuccessfully() {
    CreateShelterRequest createShelterRequest = CreateShelterRequest.builder()
        .name("Animal Shelter")
        .email("animal.shelter@email.com")
        .password("1234567890")
        .confirmPassword("1234567890")
        .build();

    Shelter actualShelter = shelterMapper.createShelterRequestToShelter(createShelterRequest);

    Assertions.assertThat(actualShelter).isNotNull();
    Assertions.assertThat(actualShelter.getId()).isNull();
    Assertions.assertThat(actualShelter.getCreatedAt()).isNull();
    Assertions.assertThat(actualShelter.getPets()).isNull();
    Assertions.assertThat(actualShelter.getName()).isNotNull().isEqualTo("Animal Shelter");
    Assertions.assertThat(actualShelter.getUser()).isNotNull();
    Assertions.assertThat(actualShelter.getUser().getId()).isNull();
    Assertions.assertThat(actualShelter.getUser().getEmail()).isNotNull().isEqualTo("animal.shelter@email.com");
    Assertions.assertThat(actualShelter.getUser().getPassword()).isNotNull().isEqualTo("1234567890");
  }

  @Test
  @DisplayName("ShelterToShelterResponse must return ShelterResponse when map successfully")
  void shelterToShelterResponse_MustReturnShelterResponse_WhenMapSuccessfully() {
    User user = User.builder()
        .id(10L)
        .email("animal.shelter@email.com")
        .password("1234567890")
        .build();

    Shelter shelter = Shelter.builder()
        .id(1000L)
        .name("Animal Shelter")
        .user(user)
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .pets(null)
        .build();

    ShelterResponse actualShelterResponse = shelterMapper
        .shelterToShelterResponse(shelter);

    Assertions.assertThat(actualShelterResponse).isNotNull();
    Assertions.assertThat(actualShelterResponse.id()).isNotNull().isEqualTo(1000L);
    Assertions.assertThat(actualShelterResponse.name()).isNotNull().isEqualTo("Animal Shelter");
    Assertions.assertThat(actualShelterResponse.email()).isNotNull().isEqualTo("animal.shelter@email.com");
  }

}