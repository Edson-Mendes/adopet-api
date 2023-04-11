package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.config.ModelMapperConfig;
import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.mapper.impl.PetMapperImpl;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.Shelter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Unit tests for PetMapperImpl")
class PetMapperImplTest {

  private PetMapperImpl petMapper;

  @BeforeEach
  void setUp() {
    petMapper = new PetMapperImpl(new ModelMapperConfig().modelMapper());
  }

  @Test
  @DisplayName("PetRequestToPet must return Pet when map successfully")
  void createPetRequestToPet_MustReturnPet_WhenMapSuccessfully() {
    CreatePetRequest createPetRequest = CreatePetRequest.builder()
        .name("Dark")
        .description("A very calm and cute cat")
        .age("3 meses")
        .shelterId(1000L)
        .build();

    Pet actualPet = petMapper.createPetRequestToPet(createPetRequest);

    Assertions.assertThat(actualPet).isNotNull();
    Assertions.assertThat(actualPet.getId()).isNull();
    Assertions.assertThat(actualPet.getCreatedAt()).isNull();
    Assertions.assertThat(actualPet.getName()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualPet.getDescription()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualPet.getAge()).isNotNull().isEqualTo("3 meses");
    Assertions.assertThat(actualPet.isAdopted()).isFalse();
    Assertions.assertThat(actualPet.getShelter().getId()).isNotNull().isEqualTo(1000L);
  }

  @Test
  @DisplayName("PetToPetResponse must return PetResponse when map successfully")
  void petToPetResponse_MustReturnPetResponse_WhenMapSuccessfully() {
    Shelter shelter = Shelter.builder()
        .id(1_000L)
        .build();
    Pet pet = Pet.builder()
        .id(10_000L)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(false)
        .shelter(shelter)
        .createdAt(LocalDateTime.parse("2023-04-10T12:00:00"))
        .build();

    PetResponse actualPetResponse = petMapper.petToPetResponse(pet);

    Assertions.assertThat(actualPetResponse).isNotNull();
    Assertions.assertThat(actualPetResponse.getId()).isNotNull().isEqualTo(10_000L);
    Assertions.assertThat(actualPetResponse.getName()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualPetResponse.getDescription()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualPetResponse.getAge()).isNotNull().isEqualTo("2 years old");
    Assertions.assertThat(actualPetResponse.isAdopted()).isFalse();
    Assertions.assertThat(actualPetResponse.getShelterId()).isNotNull().isEqualTo(1_000L);
  }

}