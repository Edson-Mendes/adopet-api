package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;
  private final PetMapper petMapper;

  @Override
  public PetResponse create(CreatePetRequest createPetRequest) {
    Pet pet = petMapper.createPetRequestToPet(createPetRequest);

    log.info("pet id : {}", pet.getId());
    pet = petRepository.save(pet);

    log.info("Pet created successfully with id : {}", pet.getId());
    return petMapper.petToPetResponse(pet);
  }

}
