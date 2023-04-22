package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdoptionService {

  AdoptionResponse adopt(AdoptionRequest adoptionRequest);

  Page<AdoptionResponse> fetchAll(Pageable pageable);

  AdoptionResponse updateStatus(Long id, UpdateStatusRequest updateStatusRequest);

  void deleteById(Long id);

}
