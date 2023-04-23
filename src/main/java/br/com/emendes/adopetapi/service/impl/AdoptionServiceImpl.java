package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.exception.AdoptionNotFoundException;
import br.com.emendes.adopetapi.exception.GuardianNotFoundException;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.model.entity.*;
import br.com.emendes.adopetapi.repository.AdoptionRepository;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.AdoptionService;
import br.com.emendes.adopetapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_GUARDIAN_NAME;
import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_SHELTER_NAME;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdoptionServiceImpl implements AdoptionService {

  private final AdoptionMapper adoptionMapper;
  private final PetRepository petRepository;
  private final AdoptionRepository adoptionRepository;
  private final UserService userService;

  @Override
  public AdoptionResponse adopt(AdoptionRequest adoptionRequest) {
    if (!petRepository.existsByIdAndAdoptedFalseAndShelterDeletedFalse(adoptionRequest.petId())) {
      log.info("Not found non adopted pet with id : {}", adoptionRequest.petId());
      throw new InvalidArgumentException("Invalid pet id");
    }

    Guardian guardian = userService.getCurrentUserAsGuardian()
        .orElseThrow(() -> new InvalidArgumentException("Current user not found"));

    Adoption adoption = adoptionMapper.adoptionRequestToAdoption(adoptionRequest);
    adoption.setGuardian(guardian);
    adoption.setDate(LocalDateTime.now());
    adoption.setStatus(AdoptionStatus.ANALYSING);

    adoption = adoptionRepository.save(adoption);
    log.info("Adoption saved successfully with id : {} and status : {}", adoption.getId(), adoption.getStatus().name());

    return adoptionMapper.adoptionToAdoptionResponse(adoption);
  }

  @Override
  public Page<AdoptionResponse> fetchAll(Pageable pageable) {
    User currentUser = userService.getCurrentUser();
    log.info("Fetching page: {}, size: {} of Adoptions", pageable.getPageNumber(), pageable.getPageSize());

    Role role = currentUser.getRoles().stream().findFirst()
        .orElseThrow(() -> new InvalidArgumentException("Not found authorities"));

    return switch (role.getName()) {
      case ROLE_SHELTER_NAME -> fetchAllForShelter(pageable);
      case ROLE_GUARDIAN_NAME -> fetchAllForGuardian(pageable);
      default -> throw new InvalidArgumentException("Unexpected value: " + role.getName());
    };
  }

  @Override
  public AdoptionResponse updateStatus(Long id, UpdateStatusRequest updateStatusRequest) {
    Adoption adoption = findAdoptionByIdAndShelter(id);
    adoption.setStatus(AdoptionStatus.valueOf(updateStatusRequest.status()));

    adoptionRepository.save(adoption);

    Pet pet = adoption.getPet();
    pet.setAdopted(adoption.getStatus().equals(AdoptionStatus.CONCLUDED));
    petRepository.save(pet);

    log.info("Status update to {} for Adoption with id : {}", updateStatusRequest.status(), adoption.getId());
    return adoptionMapper.adoptionToAdoptionResponse(adoption);
  }

  @Override
  public void deleteById(Long id) {
    Adoption adoption = findAdoptionByIdAndShelter(id);

    adoptionRepository.delete(adoption);
    log.info("Deleting Adoption with id: {}", id);
  }

  private Adoption findAdoptionByIdAndShelter(Long id) {
    Shelter shelter = getCurrentShelter();
    log.info("Searching for Adoption with id: {} and Shelter.id : {}", id, shelter.getId());
    return adoptionRepository.findByIdAndPetShelter(id, shelter)
        .orElseThrow(() -> new AdoptionNotFoundException("Adoption not found"));
  }

  private Page<AdoptionResponse> fetchAllForShelter(Pageable pageable) {
    Shelter shelter = getCurrentShelter();

    Page<Adoption> adoptionsPage = adoptionRepository.findAllByPetShelter(shelter, pageable);
    log.info("Fetching {} elements for Shelter with id : {}", adoptionsPage.getNumberOfElements(), shelter.getId());

    return adoptionsPage.map(adoptionMapper::adoptionToAdoptionResponse);
  }

  private Page<AdoptionResponse> fetchAllForGuardian(Pageable pageable) {
    Guardian guardian = userService.getCurrentUserAsGuardian()
        .orElseThrow(() -> new GuardianNotFoundException("Guardian not found"));

    Page<Adoption> adoptionsPage = adoptionRepository.findAllByGuardian(guardian, pageable);
    log.info("Fetching {} elements for Guardian with id : {}", adoptionsPage.getNumberOfElements(), guardian.getId());

    return adoptionsPage.map(adoptionMapper::adoptionToAdoptionResponse);
  }

  private Shelter getCurrentShelter() {
    return userService.getCurrentUserAsShelter()
        .orElseThrow(() -> new ShelterNotFoundException("Shelter not found"));
  }

}
