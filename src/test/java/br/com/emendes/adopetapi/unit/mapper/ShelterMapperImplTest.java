package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.config.ModelMapperConfig;
import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.mapper.impl.ShelterMapperImpl;
import br.com.emendes.adopetapi.model.entity.Shelter;
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
    shelterMapper = new ShelterMapperImpl(new ModelMapperConfig().modelMapper());
  }

  @Test
  @DisplayName("ShelterRequestToShelter must return Shelter when map successfully")
  void shelterRequestToShelter_MustReturnShelter_WhenMapSuccessfully() {
    ShelterRequest shelterRequest = ShelterRequest.builder()
        .name("Animal Shelter")
        .build();

    Shelter actualShelter = shelterMapper.shelterRequestToShelter(shelterRequest);

    Assertions.assertThat(actualShelter).isNotNull();
    Assertions.assertThat(actualShelter.getId()).isNull();
    Assertions.assertThat(actualShelter.getCreatedAt()).isNull();
    Assertions.assertThat(actualShelter.getPets()).isNull();
    Assertions.assertThat(actualShelter.getName()).isNotNull().isEqualTo("Animal Shelter");
  }

  @Test
  @DisplayName("ShelterToShelterResponse must return ShelterResponse when map successfully")
  void shelterToShelterResponse_MustReturnShelterResponse_WhenMapSuccessfully() {
    Shelter shelter = Shelter.builder()
        .id(1000L)
        .name("Animal Shelter")
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .pets(null)
        .build();

    ShelterResponse actualShelterResponse = shelterMapper
        .shelterToShelterResponse(shelter);

    Assertions.assertThat(actualShelterResponse).isNotNull();
    Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1000L);
    Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
  }

}