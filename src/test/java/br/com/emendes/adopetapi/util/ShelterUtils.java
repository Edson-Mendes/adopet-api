package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.model.entity.Shelter;

import java.time.LocalDateTime;

public abstract class ShelterUtils {

  public static ShelterResponse shelterResponse() {
    return ShelterResponse.builder()
        .id(1000L)
        .name("Animal Shelter")
        .build();
  }

  public static Shelter shelter() {
    return Shelter.builder()
        .id(1000L)
        .name("Animal Shelter")
        .createdAt(LocalDateTime.parse("2023-04-10T13:00:00"))
        .pets(null)
        .build();
  }

  public static Shelter shelterWithoutId() {
    return Shelter.builder()
        .name("Animal Shelter")
        .build();
  }

  public static Shelter updatedShelter() {
    return Shelter.builder()
        .id(1000L)
        .name("Animal Shelter ABC")
        .createdAt(LocalDateTime.parse("2023-04-10T13:00:00"))
        .pets(null)
        .build();
  }

  public static ShelterResponse updatedShelterResponse() {
    return ShelterResponse.builder()
        .id(1000L)
        .name("Animal Shelter ABC")
        .build();
  }

}
