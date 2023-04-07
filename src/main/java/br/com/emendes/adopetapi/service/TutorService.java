package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;

public interface TutorService {

  TutorResponse create(CreateTutorRequest createTutorRequest);

  TutorResponse update(Long id, UpdateTutorRequest updateTutorRequest);

  TutorResponse findById(Long id);

}
