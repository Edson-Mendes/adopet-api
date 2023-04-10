package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.config.ModelMapperConfig;
import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.mapper.impl.AnimalShelterMapperImpl;
import br.com.emendes.adopetapi.model.entity.AnimalShelter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Unit tests for AnimaShelterMapperImpl")
class AnimalShelterMapperImplTest {

  private AnimalShelterMapperImpl animalShelterMapper;

  @BeforeEach
  void setUp() {
    animalShelterMapper = new AnimalShelterMapperImpl(new ModelMapperConfig().modelMapper());
  }

  @Test
  @DisplayName("AnimalShelterRequestToAnimalShelter must return AnimalShelter when map successfully")
  void animalShelterRequestToAnimalShelter_MustReturnTutor_WhenMapSuccessfully() {
    AnimalShelterRequest shelterRequest = AnimalShelterRequest.builder()
        .name("Animal Shelter")
        .build();

    AnimalShelter actualAnimalShelter = animalShelterMapper.animalShelterRequestToAnimalShelter(shelterRequest);

    Assertions.assertThat(actualAnimalShelter).isNotNull();
    Assertions.assertThat(actualAnimalShelter.getId()).isNull();
    Assertions.assertThat(actualAnimalShelter.getCreatedAt()).isNull();
    Assertions.assertThat(actualAnimalShelter.getPets()).isNull();
    Assertions.assertThat(actualAnimalShelter.getName()).isNotNull().isEqualTo("Animal Shelter");
  }

  @Test
  @DisplayName("AnimalShelterToAnimalShelterResponse must return AnimalShelterResponse when map successfully")
  void animalShelterToAnimalShelterResponse_MustReturnAnimalShelterResponse_WhenMapSuccessfully() {
    AnimalShelter animalShelter = AnimalShelter.builder()
        .id(1000L)
        .name("Animal Shelter")
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .pets(null)
        .build();

    AnimalShelterResponse actualAnimalShelterResponse = animalShelterMapper
        .animalShelterToAnimalShelterResponse(animalShelter);

    Assertions.assertThat(actualAnimalShelterResponse).isNotNull();
    Assertions.assertThat(actualAnimalShelterResponse.getId()).isNotNull().isEqualTo(1000L);
    Assertions.assertThat(actualAnimalShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
  }

}