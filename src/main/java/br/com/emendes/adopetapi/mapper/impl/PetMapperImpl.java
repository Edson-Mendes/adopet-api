package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PetMapperImpl implements PetMapper {

  private final ModelMapper mapper;

  @Override
  public Pet createPetRequestToPet(CreatePetRequest createPetRequest) {
    return mapper.map(createPetRequest, Pet.class);
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
    pet.setName(updatePetRequest.getName());
    pet.setDescription(updatePetRequest.getDescription());
    pet.setAge(updatePetRequest.getAge());
  }

}
