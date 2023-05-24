package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Pet;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.GuardianUtils.guardian;
import static br.com.emendes.adopetapi.util.PetUtils.*;

public class AdoptionUtils {

  public static Adoption adoptionWithoutId() {
    Pet pet = Pet.builder()
        .id(10_000L)
        .build();

    return Adoption.builder()
        .id(null)
        .date(null)
        .pet(pet)
        .status(AdoptionStatus.ANALYSING)
        .build();
  }

  public static Adoption adoption() {
    return Adoption.builder()
        .id(1_000_000L)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .pet(pet())
        .guardian(guardian())
        .status(AdoptionStatus.ANALYSING)
        .build();
  }

  public static Adoption analysingAdoptionWithAdoptedPet() {
    return Adoption.builder()
        .id(1_000_000L)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .pet(adoptedPet())
        .guardian(guardian())
        .status(AdoptionStatus.ANALYSING)
        .build();
  }

  public static Adoption canceledAdoption() {
    return Adoption.builder()
        .id(1_000_000L)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .pet(pet())
        .guardian(guardian())
        .status(AdoptionStatus.CANCELED)
        .build();
  }

  public static AdoptionResponse adoptionResponse() {
    return AdoptionResponse.builder()
        .id(1_000_000L)
        .pet(petResponse())
        .guardianId(100L)
        .status(AdoptionStatus.ANALYSING)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .build();
  }

  public static AdoptionResponse canceledAdoptionResponse() {
    return AdoptionResponse.builder()
        .id(1_000_000L)
        .pet(petResponse())
        .guardianId(100L)
        .status(AdoptionStatus.CANCELED)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .build();
  }

  public static AdoptionResponse concludedAdoptionResponse() {
    return AdoptionResponse.builder()
        .id(1_000_000L)
        .pet(petResponse())
        .guardianId(100L)
        .status(AdoptionStatus.CONCLUDED)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .build();
  }


}
