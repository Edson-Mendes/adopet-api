package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.model.entity.AnimalShelter;

import java.time.LocalDateTime;

public abstract class ShelterUtils {

  public static AnimalShelterResponse animalShelterResponse() {
    return AnimalShelterResponse.builder()
        .id(1000L)
        .name("Animal Shelter")
        .build();
  }

  public static AnimalShelter animalShelter() {
    return AnimalShelter.builder()
        .id(1000L)
        .name("Animal Shelter")
        .createdAt(LocalDateTime.parse("2023-04-10T13:00:00"))
        .pets(null)
        .build();
  }

  public static AnimalShelter animalShelterWithoutId() {
    return AnimalShelter.builder()
        .name("Animal Shelter")
        .build();
  }

}
