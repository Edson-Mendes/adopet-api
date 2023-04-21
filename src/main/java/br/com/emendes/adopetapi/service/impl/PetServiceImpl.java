package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import br.com.emendes.adopetapi.exception.PetNotFoundException;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.PetService;
import br.com.emendes.adopetapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
  private final UserService userService;

  @Override
  public PetResponse create(CreatePetRequest createPetRequest) {
    Pet pet = petMapper.createPetRequestToPet(createPetRequest);

    pet.setCreatedAt(LocalDateTime.now());
    Shelter currentShelter = getCurrentShelter();
    pet.setShelter(currentShelter);

    pet = petRepository.save(pet);

    log.info("Pet created successfully with id : {}", pet.getId());
    return petMapper.petToPetResponse(pet);
  }

  @Override
  public Page<PetResponse> fetchAll(Pageable pageable) {
    Page<Pet> petPage = petRepository.findByAdoptedFalse(pageable);

    log.info("Fetching page: {}, size: {} of Pets", pageable.getPageNumber(), pageable.getPageSize());
    return petPage.map(petMapper::petToPetResponse);
  }

  @Override
  public PetResponse findById(Long id) {
    return petMapper.petToPetResponse(findPetById(id));
  }

  @Override
  public PetResponse update(Long id, UpdatePetRequest updatePetRequest) {
    Pet pet = findPetByIdAndShelter(id);

    petMapper.merge(updatePetRequest, pet);
    Pet updatedPet = petRepository.save(pet);

    log.info("Pet updated successfully with id : {}", updatedPet.getId());
    return petMapper.petToPetResponse(updatedPet);
  }

  @Override
  public void deleteById(Long id) {
    Pet pet = findPetByIdAndShelter(id);

    log.info("Deleting Pet with id: {}", id);
    try {
      petRepository.delete(pet);
    } catch (DataIntegrityViolationException exception) {
      log.info("Can not delete Pet with id : {}", id);
      log.info("Exception message : {}", exception.getMessage());
      throw new InvalidArgumentException("This pet cannot be deleted because it is in process of being adopted.");
    }
  }

  private Pet findPetById(Long id) {
    log.info("Searching for Pet with id: {}", id);
    return petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found"));
  }

  private Pet findPetByIdAndShelter(Long id) {
    Shelter shelter = getCurrentShelter();
    log.info("Searching for Pet with id: {} and Shelter.id : {}", id, shelter.getId());
    return petRepository.findByIdAndShelter(id, shelter)
        .orElseThrow(() -> new PetNotFoundException("Pet not found"));
  }

  private Shelter getCurrentShelter() {
    return userService.getCurrentUserAsShelter()
        .orElseThrow(() -> new InvalidArgumentException("Current user is not a Shelter"));
  }

}
