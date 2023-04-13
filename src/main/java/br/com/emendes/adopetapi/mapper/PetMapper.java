package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.model.entity.Pet;

public interface PetMapper {

  Pet createPetRequestToPet(CreatePetRequest createPetRequest);

  PetResponse petToPetResponse(Pet pet);

  void merge(UpdatePetRequest updatePetRequest, Pet pet);

}
