package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;

public interface ShelterService {

  ShelterResponse create(ShelterRequest shelterRequest);

}
