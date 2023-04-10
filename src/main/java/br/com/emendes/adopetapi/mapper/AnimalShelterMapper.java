package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.model.entity.AnimalShelter;

public interface AnimalShelterMapper {

  AnimalShelter animalShelterRequestToAnimalShelter(AnimalShelterRequest animalShelterRequest);

  AnimalShelterResponse animalShelterToAnimalShelterResponse(AnimalShelter animalShelter);

}
