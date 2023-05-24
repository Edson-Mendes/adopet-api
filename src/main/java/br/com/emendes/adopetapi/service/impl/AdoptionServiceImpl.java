package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.exception.AdoptionNotFoundException;
import br.com.emendes.adopetapi.exception.IllegalOperationException;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.model.entity.*;
import br.com.emendes.adopetapi.repository.AdoptionRepository;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.AdoptionService;
import br.com.emendes.adopetapi.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_GUARDIAN_NAME;
import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_SHELTER_NAME;

/**
 * Implementação de {@link AdoptionService}
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AdoptionServiceImpl implements AdoptionService {

  private final AdoptionMapper adoptionMapper;
  private final PetRepository petRepository;
  private final AdoptionRepository adoptionRepository;
  private final CurrentUserService currentUserService;

  @Override
  public AdoptionResponse adopt(AdoptionRequest adoptionRequest) {
    if (!petRepository.existsByIdAndAdoptedFalseAndShelterDeletedFalse(adoptionRequest.petId())) {
      log.info("Not found non adopted pet with id : {}", adoptionRequest.petId());
      throw new InvalidArgumentException("Invalid pet id");
    }

    Guardian guardian = getCurrentGuardian();

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
    log.info("Fetching page: {}, size: {} of Adoptions", pageable.getPageNumber(), pageable.getPageSize());

    Role role = getRoleFromCurrentUser();

    return switch (role.getName()) {
      case ROLE_SHELTER_NAME -> fetchAllForShelter(pageable);
      case ROLE_GUARDIAN_NAME -> fetchAllForGuardian(pageable);
      default -> throw new InvalidArgumentException("Unexpected value: " + role.getName());
    };
  }

  @Override
  public AdoptionResponse updateStatus(Long id, UpdateStatusRequest updateStatusRequest) {
    Adoption adoption = findAdoptionByIdAndShelter(id);
    AdoptionStatus newStatus = AdoptionStatus.valueOf(updateStatusRequest.status());

    switch (newStatus) {
      case ANALYSING -> puttingAdoptionUnderAnalysing(adoption);
      case CANCELED -> cancelingAdoption(adoption);
      case CONCLUDED -> concludingAdoption(adoption);
    }

    adoptionRepository.save(adoption);
    petRepository.save(adoption.getPet());

    log.info("Status update to {} for Adoption with id : {}", updateStatusRequest.status(), adoption.getId());
    return adoptionMapper.adoptionToAdoptionResponse(adoption);
  }

  @Override
  public AdoptionResponse findById(Long id) {
    Role role = getRoleFromCurrentUser();

    return switch (role.getName()) {
      case ROLE_SHELTER_NAME -> findByIdForShelter(id);
      case ROLE_GUARDIAN_NAME -> findByIdForGuardian(id);
      default -> throw new InvalidArgumentException("Unexpected value: " + role.getName());
    };
  }

  @Override
  public void deleteById(Long id) {
    Adoption adoption = findAdoptionByIdAndShelter(id);

    adoptionRepository.delete(adoption);
    log.info("Deleting Adoption with id: {}", id);
  }

  /**
   * Busca Adoption por id e pelo Shelter logado. Caso o User logado não seja um Shelter
   * InvalidArgumentException é lançado.
   * @param id da Adoption a ser buscado.
   * @return Adoption com o dado id.
   * @throws AdoptionNotFoundException se o id informado não pertencer a uma Adoption relacionada
   * com o User Shelter atual.
   */
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
    Guardian guardian = getCurrentGuardian();

    Page<Adoption> adoptionsPage = adoptionRepository.findAllByGuardian(guardian, pageable);
    log.info("Fetching {} elements for Guardian with id : {}", adoptionsPage.getNumberOfElements(), guardian.getId());

    return adoptionsPage.map(adoptionMapper::adoptionToAdoptionResponse);
  }

  private AdoptionResponse findByIdForShelter(Long id) {
    return adoptionMapper.adoptionToAdoptionResponse(findAdoptionByIdAndShelter(id));
  }

  private AdoptionResponse findByIdForGuardian(Long id) {
    Guardian guardian = getCurrentGuardian();
    log.info("Searching for Adoption with id: {} and Guardian.id : {}", id, guardian.getId());

    Adoption adoption = adoptionRepository.findByIdAndGuardian(id, guardian)
        .orElseThrow(() -> new AdoptionNotFoundException("Adoption not found"));
    return adoptionMapper.adoptionToAdoptionResponse(adoption);
  }

  private Guardian getCurrentGuardian() {
    return currentUserService.getCurrentUserAsGuardian()
        .orElseThrow(() -> new InvalidArgumentException("Current guardian user not found"));
  }

  private Shelter getCurrentShelter() {
    return currentUserService.getCurrentUserAsShelter()
        .orElseThrow(() -> new InvalidArgumentException("Current shelter user not found"));
  }

  private Role getRoleFromCurrentUser() {
    User currentUser = currentUserService.getCurrentUser();

    return currentUser.getRoles().stream().findFirst()
        .orElseThrow(() -> new InvalidArgumentException("Not found authorities"));
  }

  private void puttingAdoptionUnderAnalysing(Adoption adoption) {
    ifAdoptionIsConcludedSetAdoptedToFalse(adoption);
    adoption.setStatus(AdoptionStatus.ANALYSING);
  }

  private void cancelingAdoption(Adoption adoption) {
    ifAdoptionIsConcludedSetAdoptedToFalse(adoption);
    adoption.setStatus(AdoptionStatus.CANCELED);
  }

  private void concludingAdoption(Adoption adoption) {
    if (!adoption.getStatus().equals(AdoptionStatus.CONCLUDED) && adoption.getPet().isAdopted()) {
      throw new IllegalOperationException("Pet already adopted");
    }

    adoption.setStatus(AdoptionStatus.CONCLUDED);
    adoption.getPet().setAdopted(true);
  }

  private void ifAdoptionIsConcludedSetAdoptedToFalse(Adoption adoption) {
    if (adoption.getStatus().equals(AdoptionStatus.CONCLUDED)) {
      adoption.getPet().setAdopted(false);
    }
  }

}
