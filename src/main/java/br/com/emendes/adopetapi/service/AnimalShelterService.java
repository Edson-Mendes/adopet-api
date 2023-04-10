package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;

public interface AnimalShelterService {

  AnimalShelterResponse create(AnimalShelterRequest animalShelterRequest);

}
