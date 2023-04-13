package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.Shelter;
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
    return mapper.map(pet, PetResponse.class);
  }

  @Override
  public void merge(UpdatePetRequest updatePetRequest, Pet pet) {
    pet.setName(updatePetRequest.getName());
    pet.setDescription(updatePetRequest.getDescription());
    pet.setAge(updatePetRequest.getAge());
  }

}
