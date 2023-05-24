package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link AdoptionMapper}
 */
@RequiredArgsConstructor
@Component
public class AdoptionMapperImpl implements AdoptionMapper {

  @Override
  public Adoption adoptionRequestToAdoption(AdoptionRequest adoptionRequest) {
    Pet pet = Pet.builder()
        .id(adoptionRequest.petId())
        .build();

    return Adoption.builder()
        .pet(pet)
        .build();
  }

  @Override
  public AdoptionResponse adoptionToAdoptionResponse(Adoption adoption) {
    Pet pet = adoption.getPet();
    PetResponse petResponse = PetResponse.builder()
        .id(pet.getId())
        .name(pet.getName())
        .description(pet.getDescription())
        .age(pet.getAge())
        .adopted(pet.isAdopted())
        .build();

    return AdoptionResponse.builder()
        .id(adoption.getId())
        .pet(petResponse)
        .guardianId(adoption.getGuardian().getId())
        .status(adoption.getStatus())
        .date(adoption.getDate())
        .build();
  }

}
