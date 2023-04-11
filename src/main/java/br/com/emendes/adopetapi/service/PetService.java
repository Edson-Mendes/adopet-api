package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;

public interface PetService {

  PetResponse create(CreatePetRequest createPetRequest);

}
