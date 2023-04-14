package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.model.entity.Shelter;

public interface ShelterMapper {

  Shelter createShelterRequestToShelter(CreateShelterRequest createShelterRequest);

  ShelterResponse shelterToShelterResponse(Shelter shelter);

}
