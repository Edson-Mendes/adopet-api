package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.ShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

}
