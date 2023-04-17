package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.AdoptionService;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdoptionServiceImpl implements AdoptionService {

  private final AdoptionMapper adoptionMapper;
  private final AuthenticationFacade authenticationFacade;
  private final GuardianRepository guardianRepository;
  private final PetRepository petRepository;

  @Override
  public AdoptionResponse adopt(AdoptionRequest adoptionRequest) {
    //Verificar se petId is valid
    if (!petRepository.existsById(adoptionRequest.getPetId())) {
      throw new InvalidArgumentException("Invalid pet id");
    }

    //Buscar o current guardian.
    User currentUser = authenticationFacade.getCurrentUser();
    guardianRepository.findByUserId(currentUser.getId())
        .orElseThrow(() -> new InvalidArgumentException("Current user not found"));


    // Converter AdoptionRequest to Adoption



    return null;
  }

}
