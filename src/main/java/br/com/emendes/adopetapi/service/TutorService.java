package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;

public interface TutorService {

  TutorResponse create(TutorRequest tutorRequest);

}
