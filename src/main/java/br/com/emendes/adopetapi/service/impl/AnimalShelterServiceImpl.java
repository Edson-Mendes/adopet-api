package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.mapper.AnimalShelterMapper;
import br.com.emendes.adopetapi.model.entity.AnimalShelter;
import br.com.emendes.adopetapi.repository.AnimalShelterRepository;
import br.com.emendes.adopetapi.service.AnimalShelterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimalShelterServiceImpl implements AnimalShelterService {

  private final AnimalShelterMapper animalShelterMapper;
  private final AnimalShelterRepository animalShelterRepository;

  @Override
  public AnimalShelterResponse create(AnimalShelterRequest animalShelterRequest) {
    AnimalShelter animalShelter = animalShelterMapper.animalShelterRequestToAnimalShelter(animalShelterRequest);

    animalShelter.setCreatedAt(LocalDateTime.now());

    animalShelter = animalShelterRepository.save(animalShelter);

    log.info("Shelter created successfully with id : {}", animalShelter.getId());
    return animalShelterMapper.animalShelterToAnimalShelterResponse(animalShelter);
  }

}
