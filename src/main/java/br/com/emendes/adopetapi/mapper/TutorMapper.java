package br.com.emendes.adopetapi.mapper;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.model.entity.Tutor;

public interface TutorMapper {

  Tutor tutorRequestToTutor(TutorRequest tutorRequest);

}
