package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PetService {

  PetResponse create(CreatePetRequest createPetRequest);

  Page<PetResponse> fetchAll(Pageable pageable);

  PetResponse findById(Long id);

  PetResponse update(Long id, UpdatePetRequest updatePetRequest);

}
