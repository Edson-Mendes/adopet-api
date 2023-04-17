package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;

public interface AdoptionService {

  AdoptionResponse adopt(AdoptionRequest adoptionRequest);

}
