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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Override
  public TutorResponse findById(Long id) {
    return tutorMapper.tutorToTutorResponse(findTutorById(id));
  }

  @Override
  public Page<TutorResponse> fetchAll(Pageable pageable) {
    Page<Tutor> tutorPage = tutorRepository.findAll(pageable);

    log.info("Fetching page: {}, size: {} of Tutors", pageable.getPageNumber(), pageable.getPageSize());
    return tutorPage.map(tutorMapper::tutorToTutorResponse);
  }

  @Override
  public void deleteById(Long id) {
    Tutor tutor = findTutorById(id);

    log.info("Deleting Shelter with id: {}", id);
    tutorRepository.delete(tutor);
  }

  private Tutor findTutorById(Long id) {
    log.info("Searching for Tutor with id: {}", id);
    return tutorRepository.findById(id).orElseThrow(() -> new TutorNotFoundException("Tutor not found"));
  }

}
