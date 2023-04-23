package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.ShelterService;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_SHELTER;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShelterServiceImpl implements ShelterService {

  private final ShelterMapper shelterMapper;
  private final ShelterRepository shelterRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationFacade authenticationFacade;

  @Override
  public ShelterResponse create(CreateShelterRequest createShelterRequest) {
    if (!createShelterRequest.password().equals(createShelterRequest.confirmPassword())) {
      log.info("Passwords do not match at ShelterServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }
    Shelter shelter = shelterMapper.createShelterRequestToShelter(createShelterRequest);
    shelter.setCreatedAt(LocalDateTime.now());
    shelter.getUser().addRole(ROLE_SHELTER);

    shelter.getUser().setPassword(passwordEncoder.encode(createShelterRequest.password()));
    shelter.getUser().setEnabled(true);

    try {
      Shelter savedShelter = shelterRepository.save(shelter);
      log.info("Shelter saved successfully with id : {}", savedShelter.getId());

      return shelterMapper.shelterToShelterResponse(savedShelter);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String
          .format("E-mail {%s} is already in use", createShelterRequest.email()));
    }
  }

  @Override
  public Page<ShelterResponse> fetchAll(Pageable pageable) {
    Page<Shelter> shelterPage = shelterRepository.findByDeletedFalse(pageable);

    log.info("Fetching page: {}, size: {} of Shelters", pageable.getPageNumber(), pageable.getPageSize());
    return shelterPage.map(shelterMapper::shelterToShelterResponse);
  }

  @Override
  public ShelterResponse findById(Long id) {
    return shelterMapper.shelterToShelterResponse(findShelterById(id));
  }

  @Override
  public ShelterResponse update(Long id, UpdateShelterRequest updateShelterRequest) {
    Shelter shelter = findShelterByIdAndUser(id);

    shelter.setName(updateShelterRequest.name());
    shelter.getUser().setEmail(updateShelterRequest.email());

    try {
      Shelter updatedShelter = shelterRepository.save(shelter);
      log.info("Shelter updated successfully with id : {}", updatedShelter.getId());
      return shelterMapper.shelterToShelterResponse(updatedShelter);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String.format("E-mail {%s} is already in use", updateShelterRequest.email()));
    }
  }

  @Override
  public void deleteById(Long id) {
    Shelter shelter = findShelterByIdAndUser(id);

    log.info("Setting deleted to true on Shelter with id: {}", id);

    shelter.getUser().setEnabled(false);
    shelter.setDeleted(true);

    shelterRepository.save(shelter);
  }

  private Shelter findShelterById(Long id) {
    log.info("Searching for Shelter with id: {}", id);
    return shelterRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ShelterNotFoundException("Shelter not found"));
  }

  private Shelter findShelterByIdAndUser(Long id) {
    User currentUser = authenticationFacade.getCurrentUser();
    log.info("Searching for Shelter with id: {} and User.id : {}", id, currentUser.getId());
    return shelterRepository.findByIdAndUserAndDeletedFalse(id, currentUser)
        .orElseThrow(() -> new ShelterNotFoundException("Shelter not found"));
  }

}
