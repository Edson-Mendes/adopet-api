package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.Shelter;

import java.time.LocalDateTime;

public abstract class PetUtils {

  public static Pet pet() {
    return Pet.builder()
        .id(10_000L)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(false)
        .createdAt(LocalDateTime.parse("2022-04-10T12:00:00"))
        .shelter(ShelterUtils.shelter())
        .build();
  }

  public static Pet updatedPet() {
    return Pet.builder()
        .id(10_000L)
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .adopted(false)
        .createdAt(LocalDateTime.parse("2022-04-10T12:00:00"))
        .shelter(ShelterUtils.shelter())
        .build();
  }

  public static Pet petWithoutId() {
    Shelter shelter = Shelter.builder()
        .id(1_000L)
        .build();
    return Pet.builder()
        .id(null)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(false)
        .createdAt(LocalDateTime.parse("2022-04-10T12:00:00"))
        .shelter(shelter)
        .build();
  }

  public static PetResponse petResponse() {
    return PetResponse.builder()
        .id(10_000L)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(false)
        .shelterId(1_000L)
        .build();
  }

  public static PetResponse updatedPetResponse() {
    return PetResponse.builder()
        .id(10_000L)
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .adopted(false)
        .shelterId(1_000L)
        .build();
  }

}
