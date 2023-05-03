package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.model.entity.Pet;

import java.time.LocalDateTime;
import java.util.List;

import static br.com.emendes.adopetapi.util.PetImageUtils.*;

public abstract class PetUtils {

  public static Pet pet() {
    return Pet.builder()
        .id(10_000L)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(false)
        .images(petImageList())
        .createdAt(LocalDateTime.parse("2022-04-10T12:00:00"))
        .shelter(ShelterUtils.shelter())
        .build();
  }

  public static Pet adoptedPet() {
    return Pet.builder()
        .id(10_000L)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(true)
        .images(petImageList())
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
        .images(petImageList())
        .createdAt(LocalDateTime.parse("2022-04-10T12:00:00"))
        .shelter(ShelterUtils.shelter())
        .build();
  }

  public static Pet petWithoutId() {
    return Pet.builder()
        .id(null)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(false)
        .images(petImageWithoutIdList())
        .createdAt(LocalDateTime.parse("2022-04-10T12:00:00"))
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
        .images(List.of(imageResponse()))
        .build();
  }

  public static PetResponse updatedPetResponse() {
    return PetResponse.builder()
        .id(10_000L)
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .adopted(false)
        .images(List.of(imageResponse()))
        .shelterId(1_000L)
        .build();
  }

}
