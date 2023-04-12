package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GuardianService {

  GuardianResponse create(CreateGuardianRequest createGuardianRequest);

  GuardianResponse update(Long id, UpdateGuardianRequest updateGuardianRequest);

  GuardianResponse findById(Long id);

  Page<GuardianResponse> fetchAll(Pageable pageable);

  void deleteById(Long id);

}
