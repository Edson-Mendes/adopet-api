package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.ShelterService;
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
public class ShelterServiceImpl implements ShelterService {

  private final ShelterMapper shelterMapper;
  private final ShelterRepository shelterRepository;

  @Override
  public ShelterResponse create(CreateShelterRequest createShelterRequest) {
    if (!createShelterRequest.getPassword().equals(createShelterRequest.getConfirmPassword())) {
      log.info("Passwords do not match at ShelterServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }
    Shelter shelter = shelterMapper.createShelterRequestToShelter(createShelterRequest);
    shelter.setCreatedAt(LocalDateTime.now());

    // TODO: Criptografar Guardian.password antes de salvar no DB.

    try {
      Shelter savedShelter = shelterRepository.save(shelter);
      log.info("Shelter saved successfully with id : {}", savedShelter.getId());

      return shelterMapper.shelterToShelterResponse(savedShelter);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String
          .format("E-mail {%s} is already in use", createShelterRequest.getEmail()));
    }
  }

  @Override
  public Page<ShelterResponse> fetchAll(Pageable pageable) {
    Page<Shelter> shelterPage = shelterRepository.findAll(pageable);

    log.info("Fetching page: {}, size: {} of Shelters", pageable.getPageNumber(), pageable.getPageSize());
    return shelterPage.map(shelterMapper::shelterToShelterResponse);
  }

  @Override
  public ShelterResponse findById(Long id) {
    return shelterMapper.shelterToShelterResponse(findShelterById(id));
  }

  @Override
  public ShelterResponse update(Long id, UpdateShelterRequest updateShelterRequest) {
    Shelter shelter = findShelterById(id);

    shelter.setName(updateShelterRequest.getName());
    Shelter updatedShelter = shelterRepository.save(shelter);

    log.info("Shelter updated successfully with id : {}", updatedShelter.getId());
    return shelterMapper.shelterToShelterResponse(updatedShelter);
  }

  @Override
  public void deleteById(Long id) {
    Shelter shelter = findShelterById(id);

    log.info("Deleting Shelter with id: {}", id);
    shelterRepository.delete(shelter);
  }

  private Shelter findShelterById(Long id) {
    log.info("Searching for Shelter with id: {}", id);
    return shelterRepository.findById(id).orElseThrow(() -> new ShelterNotFoundException("Shelter not found"));
  }

}
