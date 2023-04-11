package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelterService {

  ShelterResponse create(ShelterRequest shelterRequest);

  Page<ShelterResponse> fetchAll(Pageable pageable);

  ShelterResponse findById(Long id);

  ShelterResponse update(Long id, ShelterRequest shelterRequest);

  void deleteById(Long id);

}
