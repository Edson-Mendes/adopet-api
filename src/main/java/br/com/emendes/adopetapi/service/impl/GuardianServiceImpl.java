package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.GuardianNotFoundException;
import br.com.emendes.adopetapi.mapper.GuardianMapper;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.service.GuardianService;
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
public class GuardianServiceImpl implements GuardianService {

  private final GuardianRepository guardianRepository;
  private final GuardianMapper guardianMapper;

  @Override
  public GuardianResponse create(CreateGuardianRequest createGuardianRequest) {
    if (!createGuardianRequest.isPasswordsMatch()) {
      log.info("Passwords do not match at GuardianServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }

    Guardian guardian = guardianMapper.guardianRequestToGuardian(createGuardianRequest);
    guardian.setCreatedAt(LocalDateTime.now());

    // TODO: Criptografar Guardian.password antes de salvar no DB.

    try {
      Guardian savedGuardian = guardianRepository.save(guardian);
      log.info("Guardian saved successfully with id : {}", savedGuardian.getId());
      return guardianMapper.guardianToGuardianResponse(savedGuardian);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String.format("E-mail {%s} is already in use", createGuardianRequest.getEmail()));
    }

  }

  @Override
  public GuardianResponse update(Long id, UpdateGuardianRequest updateGuardianRequest) {
    Guardian guardian = findGuardianById(id);

    guardian.setName(updateGuardianRequest.getName());
    guardian.setEmail(updateGuardianRequest.getEmail());

    try {
      Guardian updatedGuardian = guardianRepository.save(guardian);
      log.info("Guardian updated successfully with id : {}", updatedGuardian.getId());
      return guardianMapper.guardianToGuardianResponse(updatedGuardian);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String.format("E-mail {%s} is already in use", updateGuardianRequest.getEmail()));
    }
  }

  @Override
  public GuardianResponse findById(Long id) {
    return guardianMapper.guardianToGuardianResponse(findGuardianById(id));
  }

  @Override
  public Page<GuardianResponse> fetchAll(Pageable pageable) {
    Page<Guardian> guardianPage = guardianRepository.findAll(pageable);

    log.info("Fetching page: {}, size: {} of Guardians", pageable.getPageNumber(), pageable.getPageSize());
    return guardianPage.map(guardianMapper::guardianToGuardianResponse);
  }

  @Override
  public void deleteById(Long id) {
    Guardian guardian = findGuardianById(id);

    log.info("Deleting Shelter with id: {}", id);
    guardianRepository.delete(guardian);
  }

  private Guardian findGuardianById(Long id) {
    log.info("Searching for Guardian with id: {}", id);
    return guardianRepository.findById(id).orElseThrow(() -> new GuardianNotFoundException("Guardian not found"));
  }

}
