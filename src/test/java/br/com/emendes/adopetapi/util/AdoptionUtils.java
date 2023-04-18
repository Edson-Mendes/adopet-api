package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Pet;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.GuardianUtils.guardian;
import static br.com.emendes.adopetapi.util.PetUtils.pet;

public class AdoptionUtils {

  public static Adoption adoptionWithoutId() {
    Pet pet = Pet.builder()
        .id(10_000L)
        .build();

    return Adoption.builder()
        .id(null)
        .date(null)
        .pet(pet)
        .build();
  }

  public static Adoption adoption() {
    return Adoption.builder()
        .id(1_000_000L)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .pet(pet())
        .guardian(guardian())
        .build();
  }

  public static AdoptionResponse adoptionResponse() {
    return AdoptionResponse.builder()
        .id(1_000_000L)
        .petId(10_000L)
        .guardianId(100L)
        .status(AdoptionStatus.ANALYSING)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .build();
  }

}
