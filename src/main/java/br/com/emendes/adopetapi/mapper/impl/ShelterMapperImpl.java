package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
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
  public Shelter createShelterRequestToShelter(CreateShelterRequest createShelterRequest) {
    return mapper.map(createShelterRequest, Shelter.class);
  }

  @Override
  public ShelterResponse shelterToShelterResponse(Shelter shelter) {
    return mapper.map(shelter, ShelterResponse.class);
  }

}
