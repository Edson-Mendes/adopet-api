package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.TutorNotFoundException;
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
  public TutorResponse create(CreateTutorRequest createTutorRequest) {
    if (!createTutorRequest.isPasswordsMatch()) {
      log.info("Passwords do not match at TutorServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }

    Tutor tutor = tutorMapper.tutorRequestToTutor(createTutorRequest);
    tutor.setCreatedAt(LocalDateTime.now());

    // TODO: Criptografar Tutor.password antes de salvar no DB.

    try {
      Tutor savedTutor = tutorRepository.save(tutor);
      log.info("Tutor saved successfully with id : {}", savedTutor.getId());
      return tutorMapper.tutorToTutorResponse(savedTutor);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String.format("E-mail {%s} is already in use", createTutorRequest.getEmail()));
    }

  }

  @Override
  public TutorResponse update(Long id, UpdateTutorRequest updateTutorRequest) {
    Tutor tutor = findTutorById(id);

    tutor.setName(updateTutorRequest.getName());
    tutor.setEmail(updateTutorRequest.getEmail());

    try {
      Tutor updatedTutor = tutorRepository.save(tutor);
      log.info("Tutor updated successfully with id : {}", updatedTutor.getId());
      return tutorMapper.tutorToTutorResponse(updatedTutor);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String.format("E-mail {%s} is already in use", updateTutorRequest.getEmail()));
    }
  }

  private Tutor findTutorById(Long id) {
    return tutorRepository.findById(id).orElseThrow(() -> new TutorNotFoundException("Tutor not found"));
  }

}
