package br.com.emendes.adopetapi.mapper.impl;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.mapper.TutorMapper;
import br.com.emendes.adopetapi.model.entity.Tutor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TutorMapperImpl implements TutorMapper {

  private ModelMapper modelMapper;

  @Override
  public Tutor tutorRequestToTutor(TutorRequest tutorRequest) {
    return modelMapper.map(tutorRequest, Tutor.class);
  }

}
