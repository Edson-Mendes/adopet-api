package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorService {

  TutorResponse create(CreateTutorRequest createTutorRequest);

  TutorResponse update(Long id, UpdateTutorRequest updateTutorRequest);

  TutorResponse findById(Long id);

  Page<TutorResponse> fetchAll(Pageable pageable);

}
