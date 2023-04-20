package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.GuardianNotFoundException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.mapper.GuardianMapper;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.service.GuardianService;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_GUARDIAN;

@Slf4j
@RequiredArgsConstructor
@Service
public class GuardianServiceImpl implements GuardianService {

  private final GuardianRepository guardianRepository;
  private final GuardianMapper guardianMapper;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationFacade authenticationFacade;

  @Override
  public GuardianResponse create(CreateGuardianRequest createGuardianRequest) {
    if (!createGuardianRequest.getPassword().equals(createGuardianRequest.getConfirmPassword())) {
      log.info("Passwords do not match at GuardianServiceImpl#create");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }

    Guardian guardian = guardianMapper.createGuardianRequestToGuardian(createGuardianRequest);
    guardian.setCreatedAt(LocalDateTime.now());
    guardian.getUser().addRole(ROLE_GUARDIAN);

    guardian.getUser().setPassword(passwordEncoder.encode(createGuardianRequest.getPassword()));
    guardian.getUser().setEnabled(true);

    try {
      Guardian savedGuardian = guardianRepository.save(guardian);
      log.info("Guardian saved successfully with id : {}", savedGuardian.getId());

      return guardianMapper.guardianToGuardianResponse(savedGuardian);
    } catch (DataIntegrityViolationException exception) {
      log.info("Data Integrity Violation, message : {}", exception.getMessage());
      throw new EmailAlreadyInUseException(String
          .format("E-mail {%s} is already in use", createGuardianRequest.getEmail()));
    }
  }

  @Override
  public GuardianResponse update(Long id, UpdateGuardianRequest updateGuardianRequest) {
    Guardian guardian = findGuardianByIdAndUser(id);

    guardian.setName(updateGuardianRequest.getName());
    guardian.getUser().setEmail(updateGuardianRequest.getEmail());

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

    // FIXME: Essa busca está fazendo 3 consultas ao banco de dados.
    // 1 - para trazer os dados de Guardian.
    // 2 e 3 - Para trazer dados de User.
    log.info("Fetching page: {}, size: {} of Guardians", pageable.getPageNumber(), pageable.getPageSize());
    return guardianPage.map(guardianMapper::guardianToGuardianResponse);
  }

  @Override
  public void deleteById(Long id) {
    Guardian guardian = findGuardianByIdAndUser(id);

    guardian.getUser().setEnabled(false);
    guardianRepository.save(guardian);

    log.info("Disable Guardian with id: {}", id);
  }

  private Guardian findGuardianById(Long id) {
    log.info("Searching for Guardian with id: {}", id);
    return guardianRepository.findById(id).orElseThrow(() -> new GuardianNotFoundException("Guardian not found"));
  }

  private Guardian findGuardianByIdAndUser(Long id) {
    User currentUser = authenticationFacade.getCurrentUser();
    log.info("Searching for Guardian with id: {} and User.id : {}", id, currentUser.getId());
    return guardianRepository.findByIdAndUser(id, currentUser).orElseThrow(() -> new GuardianNotFoundException("Guardian not found"));
  }

}
