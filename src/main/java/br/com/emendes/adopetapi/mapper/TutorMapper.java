package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.model.entity.Tutor;

public interface TutorMapper {

  Tutor tutorRequestToTutor(CreateTutorRequest createTutorRequest);

  TutorResponse tutorToTutorResponse(Tutor tutor);

}
