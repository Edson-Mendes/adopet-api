package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.AdoptionRepository;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.AdoptionService;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdoptionServiceImpl implements AdoptionService {

  private final AdoptionMapper adoptionMapper;
  private final AuthenticationFacade authenticationFacade;
  private final GuardianRepository guardianRepository;
  private final PetRepository petRepository;
  private final AdoptionRepository adoptionRepository;

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

}
