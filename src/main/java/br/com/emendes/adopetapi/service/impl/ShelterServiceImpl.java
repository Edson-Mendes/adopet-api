package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.ShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  public ShelterResponse create(ShelterRequest shelterRequest) {
    Shelter shelter = shelterMapper.shelterRequestToShelter(shelterRequest);

    shelter.setCreatedAt(LocalDateTime.now());

    shelter = shelterRepository.save(shelter);

    log.info("Shelter created successfully with id : {}", shelter.getId());
    return shelterMapper.shelterToShelterResponse(shelter);
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
  public ShelterResponse update(Long id, ShelterRequest shelterRequest) {
    Shelter shelter = findShelterById(id);

    shelter.setName(shelterRequest.getName());
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
