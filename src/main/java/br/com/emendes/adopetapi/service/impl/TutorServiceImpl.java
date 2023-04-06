package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.mapper.TutorMapper;
import br.com.emendes.adopetapi.model.entity.Tutor;
import br.com.emendes.adopetapi.repository.TutorRepository;
import br.com.emendes.adopetapi.service.TutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class TutorServiceImpl implements TutorService {

  private final TutorRepository tutorRepository;
  private final TutorMapper tutorMapper;

  @Override
  public TutorResponse create(TutorRequest tutorRequest) {
    if (!tutorRequest.isPasswordsMatch()) {
      log.info("Passwords do not match at TutorServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }

    Tutor tutor = tutorMapper.tutorRequestToTutor(tutorRequest);
    tutor.setCreatedAt(LocalDateTime.now());

    // TODO: Criptografar Tutor.password antes de salvar no DB.

    try {
      Tutor tutorSaved = tutorRepository.save(tutor);
      log.info("Tutor saved successfully with id : {}", tutorSaved.getId());
      return tutorMapper.tutorToTutorResponse(tutorSaved);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String.format("E-mail {%s} is already in use", tutorRequest.getEmail()));
    }

  }

}
