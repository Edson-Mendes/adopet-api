package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.exception.GuardianNotFoundException;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.entity.*;
import br.com.emendes.adopetapi.repository.AdoptionRepository;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.AdoptionService;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
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

  // TODO: Extrair essas chamadas de repositories para services.
  private final AdoptionMapper adoptionMapper;
  private final AuthenticationFacade authenticationFacade;
  private final GuardianRepository guardianRepository;
  private final PetRepository petRepository;
  private final AdoptionRepository adoptionRepository;
  private final ShelterRepository shelterRepository;

  @Override
  public AdoptionResponse adopt(AdoptionRequest adoptionRequest) {
    if (!petRepository.existsById(adoptionRequest.getPetId())) {
      throw new InvalidArgumentException("Invalid pet id");
    }
    log.info("exists pet with id : {}", adoptionRequest.getPetId());

    User currentUser = authenticationFacade.getCurrentUser();
    Guardian guardian = guardianRepository.findByUserId(currentUser.getId())
        .orElseThrow(() -> new InvalidArgumentException("Current user not found"));
    log.info("found guardian with id : {}", guardian.getId());

    Adoption adoption = adoptionMapper.adoptionRequestToAdoption(adoptionRequest);
    adoption.setGuardian(guardian);
    adoption.setDate(LocalDateTime.now());
    log.info("Converting AdoptionRequest to Adoption");

    adoption = adoptionRepository.save(adoption);
    log.info("Adoptions saved successfully with id : {}", adoption.getId());
    log.info("Adoptions date : {}", adoption.getDate());

    return adoptionMapper.adoptionToAdoptionResponse(adoption);
  }

  @Override
  public Page<AdoptionResponse> fetchAll(Pageable pageable) {
    User currentUser = authenticationFacade.getCurrentUser();
    log.info("Fetching page: {}, size: {} of Adoptions", pageable.getPageNumber(), pageable.getPageSize());

    Role role = currentUser.getRoles().stream().findFirst()
        .orElseThrow(() -> new InvalidArgumentException("Not found authorities"));

    return switch (role.getName()) {
      case ROLE_SHELTER_NAME -> fetchAllForShelter(pageable, currentUser);
      case ROLE_GUARDIAN_NAME -> fetchAllForGuardian(pageable, currentUser);
      default -> throw new InvalidArgumentException("Unexpected value: " + role.getName());
    };
  }

  private Page<AdoptionResponse> fetchAllForShelter(Pageable pageable, User user) {
    Shelter shelter = shelterRepository.findByUserId(user.getId())
        .orElseThrow(() -> new ShelterNotFoundException("Shelter not found"));

    Page<Adoption> adoptionsPage = adoptionRepository.findAllByPetShelter(shelter, pageable);
    log.info("Fetching {} elements for Shelter with id : {}", adoptionsPage.getNumberOfElements(), shelter.getId());

    return adoptionsPage.map(adoptionMapper::adoptionToAdoptionResponse);
  }

  private Page<AdoptionResponse> fetchAllForGuardian(Pageable pageable, User user) {
    Guardian guardian = guardianRepository.findByUserId(user.getId())
        .orElseThrow(() -> new GuardianNotFoundException("Guardian not found"));

    Page<Adoption> adoptionsPage = adoptionRepository.findAllByGuardian(guardian, pageable);
    log.info("Fetching {} elements for Guardian with id : {}", adoptionsPage.getNumberOfElements(), guardian.getId());

    return adoptionsPage.map(adoptionMapper::adoptionToAdoptionResponse);
  }


}
