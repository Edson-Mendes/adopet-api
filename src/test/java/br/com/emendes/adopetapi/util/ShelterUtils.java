package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.model.entity.Shelter;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.UserUtils.shelterUser;
import static br.com.emendes.adopetapi.util.UserUtils.shelterUserWithoutId;

public abstract class ShelterUtils {

  public static ShelterResponse shelterResponse() {
    return ShelterResponse.builder()
        .id(1000L)
        .name("Animal Shelter")
        .email("animal.shelter@email.com")
        .build();
  }

  public static Shelter shelter() {
    return Shelter.builder()
        .id(1000L)
        .name("Animal Shelter")
        .user(shelterUser())
        .createdAt(LocalDateTime.parse("2023-04-10T13:00:00"))
        .pets(null)
        .build();
  }

  public static Shelter shelterWithoutId() {
    return Shelter.builder()
        .name("Animal Shelter")
        .user(shelterUserWithoutId())
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
