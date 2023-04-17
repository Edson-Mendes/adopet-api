package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.model.entity.Adoption;

public interface AdoptionMapper {

  Adoption adoptionRequestToAdoption(AdoptionRequest adoptionRequest);

}
