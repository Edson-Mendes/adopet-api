package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelterService {

  ShelterResponse create(CreateShelterRequest createShelterRequest);

  Page<ShelterResponse> fetchAll(Pageable pageable);

  ShelterResponse findById(Long id);

  ShelterResponse update(Long id, UpdateShelterRequest updateShelterRequest);

  void deleteById(Long id);

}
