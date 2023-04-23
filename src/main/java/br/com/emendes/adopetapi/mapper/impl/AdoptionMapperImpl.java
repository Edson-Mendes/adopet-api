package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Pet;
import org.springframework.stereotype.Component;

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
    return AdoptionResponse.builder()
        .id(adoption.getId())
        .petId(adoption.getPet().getId())
        .guardianId(adoption.getGuardian().getId())
        .status(adoption.getStatus())
        .date(adoption.getDate())
        .build();
  }

}
