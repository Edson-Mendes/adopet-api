package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
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
    petMapper = new PetMapperImpl();
  }

  @Test
  @DisplayName("PetRequestToPet must return Pet when map successfully")
  void createPetRequestToPet_MustReturnPet_WhenMapSuccessfully() {
    CreatePetRequest createPetRequest = CreatePetRequest.builder()
        .name("Dark")
        .description("A very calm and cute cat")
        .age("3 months old")
        .build();

    Pet actualPet = petMapper.createPetRequestToPet(createPetRequest);

    Assertions.assertThat(actualPet).isNotNull();
    Assertions.assertThat(actualPet.getId()).isNull();
    Assertions.assertThat(actualPet.getCreatedAt()).isNull();
    Assertions.assertThat(actualPet.getName()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualPet.getDescription()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualPet.getAge()).isNotNull().isEqualTo("3 months old");
    Assertions.assertThat(actualPet.isAdopted()).isFalse();
    Assertions.assertThat(actualPet.getShelter()).isNull();
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
    Assertions.assertThat(actualPetResponse.id()).isNotNull().isEqualTo(10_000L);
    Assertions.assertThat(actualPetResponse.name()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualPetResponse.description()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualPetResponse.age()).isNotNull().isEqualTo("2 years old");
    Assertions.assertThat(actualPetResponse.adopted()).isFalse();
    Assertions.assertThat(actualPetResponse.shelterId()).isNotNull().isEqualTo(1_000L);
  }

  @Test
  @DisplayName("Merge must return Pet merged with data from UpdatePetRequest when merge successfully")
  void merge_MustReturnPetMergedWithDataFromUpdatePetRequest_WhenMergeSuccessfully() {
    Shelter shelter = Shelter.builder()
        .id(1_000L)
        .build();
    Pet pet = Pet.builder()
        .id(10_000L)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .createdAt(LocalDateTime.parse("2023-04-10T12:00:00"))
        .adopted(false)
        .shelter(shelter)
        .build();

    UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
        .name("Fluffy")
        .description("A cute cat")
        .age("3 years old")
        .build();

    petMapper.merge(updatePetRequest, pet);

    Assertions.assertThat(pet).isNotNull();
    Assertions.assertThat(pet.getId()).isNotNull().isEqualTo(10_000L);
    Assertions.assertThat(pet.getCreatedAt()).isNotNull().isEqualTo("2023-04-10T12:00:00");
    Assertions.assertThat(pet.isAdopted()).isFalse();
    Assertions.assertThat(pet.getShelter()).isNotNull();
    Assertions.assertThat(pet.getShelter().getId()).isNotNull().isEqualTo(1_000L);
    Assertions.assertThat(pet.getName()).isNotNull().isEqualTo("Fluffy");
    Assertions.assertThat(pet.getDescription()).isNotNull().isEqualTo("A cute cat");
    Assertions.assertThat(pet.getAge()).isNotNull().isEqualTo("3 years old");
  }

}