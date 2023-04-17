package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.entity.Adoption;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AdoptionMapperImpl implements AdoptionMapper {

  private final ModelMapper mapper;

  @Override
  public Adoption adoptionRequestToAdoption(AdoptionRequest adoptionRequest) {
    return mapper.map(adoptionRequest, Adoption.class);
  }

  @Override
  public AdoptionResponse adoptionToAdoptionResponse(Adoption adoption) {
    return mapper.map(adoption, AdoptionResponse.class);
  }

}
