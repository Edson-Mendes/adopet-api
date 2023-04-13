package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.exception.PetNotFoundException;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;
  private final PetMapper petMapper;

  @Override
  public PetResponse create(CreatePetRequest createPetRequest) {
    Pet pet = petMapper.createPetRequestToPet(createPetRequest);

    pet.setCreatedAt(LocalDateTime.now());
    // FIXME: Caso shelter não corresponda a nenhuma linha em t_shelter uma exception é lançada!
    pet = petRepository.save(pet);

    log.info("Pet created successfully with id : {}", pet.getId());
    return petMapper.petToPetResponse(pet);
  }

  @Override
  public Page<PetResponse> fetchAll(Pageable pageable) {
    Page<Pet> petPage = petRepository.findAll(pageable);

    log.info("Fetching page: {}, size: {} of Pets", pageable.getPageNumber(), pageable.getPageSize());
    return petPage.map(petMapper::petToPetResponse);
  }

  @Override
  public PetResponse findById(Long id) {
    return petMapper.petToPetResponse(findPetById(id));
  }

  @Override
  public PetResponse update(Long id, UpdatePetRequest updatePetRequest) {
    Pet pet = findPetById(id);

    petMapper.merge(updatePetRequest, pet);
    Pet updatedPet = petRepository.save(pet);

    log.info("Pet updated successfully with id : {}", updatedPet.getId());
    return petMapper.petToPetResponse(updatedPet);
  }

  private Pet findPetById(Long id) {
    log.info("Searching for Pet with id: {}", id);
    return petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found"));
  }

}
