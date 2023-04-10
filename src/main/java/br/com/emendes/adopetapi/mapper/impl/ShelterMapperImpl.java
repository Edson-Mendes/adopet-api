package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ShelterMapperImpl implements ShelterMapper {

  private final ModelMapper mapper;

  @Override
  public Shelter shelterRequestToShelter(ShelterRequest shelterRequest) {
    return mapper.map(shelterRequest, Shelter.class);
  }

  @Override
  public ShelterResponse shelterToShelterResponse(Shelter shelter) {
    return mapper.map(shelter, ShelterResponse.class);
  }

}
