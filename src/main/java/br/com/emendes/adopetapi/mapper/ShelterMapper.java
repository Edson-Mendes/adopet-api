package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.model.entity.Shelter;

public interface ShelterMapper {

  Shelter shelterRequestToShelter(ShelterRequest shelterRequest);

  ShelterResponse shelterToShelterResponse(Shelter shelter);

}
