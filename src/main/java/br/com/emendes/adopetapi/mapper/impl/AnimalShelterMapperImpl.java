package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.mapper.AnimalShelterMapper;
import br.com.emendes.adopetapi.model.entity.AnimalShelter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AnimalShelterMapperImpl implements AnimalShelterMapper {

  private final ModelMapper mapper;

  @Override
  public AnimalShelter animalShelterRequestToAnimalShelter(AnimalShelterRequest animalShelterRequest) {
    return mapper.map(animalShelterRequest, AnimalShelter.class);
  }

  @Override
  public AnimalShelterResponse animalShelterToAnimalShelterResponse(AnimalShelter animalShelter) {
    return mapper.map(animalShelter, AnimalShelterResponse.class);
  }

}
