package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapperImpl implements PetMapper {

  @Override
  public Pet createPetRequestToPet(CreatePetRequest createPetRequest) {
    return Pet.builder()
        .name(createPetRequest.name())
        .description(createPetRequest.description())
        .age(createPetRequest.age())
        .build();
  }

  @Override
  public PetResponse petToPetResponse(Pet pet) {
    return PetResponse.builder()
        .id(pet.getId())
        .name(pet.getName())
        .description(pet.getDescription())
        .age(pet.getAge())
        .adopted(pet.isAdopted())
        .shelterId(pet.getShelter().getId())
        .build();
  }

  @Override
  public void merge(UpdatePetRequest updatePetRequest, Pet pet) {
    pet.setName(updatePetRequest.name());
    pet.setDescription(updatePetRequest.description());
    pet.setAge(updatePetRequest.age());
  }

}
