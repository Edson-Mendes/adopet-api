package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.mapper.TutorMapper;
import br.com.emendes.adopetapi.model.entity.Tutor;
import br.com.emendes.adopetapi.repository.TutorRepository;
import br.com.emendes.adopetapi.service.TutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class TutorServiceImpl implements TutorService {

  private final TutorRepository tutorRepository;
  private final TutorMapper tutorMapper;

  @Override
  public TutorResponse create(@Valid TutorRequest tutorRequest) {
    if (!tutorRequest.isPasswordsMatch()) {
      log.info("Passwords do not match at TutorServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }

    Tutor tutor = tutorMapper.tutorRequestToTutor(tutorRequest);
    tutor.setCreatedAt(LocalDateTime.now());

    // TODO: Criptografar Tutor.password antes de salvar no DB.
    // TODO: Fazer tratamento para caso o email já esteja em uso

    Tutor tutorSaved = tutorRepository.save(tutor);

    return tutorMapper.tutorToTutorResponse(tutorSaved);
  }

}
