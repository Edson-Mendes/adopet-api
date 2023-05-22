package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.WrongPasswordException;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.UserRepository;
import br.com.emendes.adopetapi.service.CurrentUserService;
import br.com.emendes.adopetapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementação de {@link UserService}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final CurrentUserService currentUserService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  /**
   * @throws PasswordsDoNotMatchException se UpdatePasswordRequest.newPassword e UpdatePasswordRequest.confirmPassword
   * forem diferentes.
   * @throws WrongPasswordException se UpdatePasswordRequest.oldPassword for diferente da senha atual do usuário.
   */
  @Override
  public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
    if (!updatePasswordRequest.newPassword().equals(updatePasswordRequest.confirmPassword())) {
      log.info("Passwords do not match");
      throw new PasswordsDoNotMatchException("Passwords do not match");
    }
    User user = currentUserService.getCurrentUser();

    if (!passwordEncoder.matches(updatePasswordRequest.oldPassword(), user.getPassword())) {
      log.info("Wrong Password");
      throw new WrongPasswordException("Old password is wrong");
    }

    user.setPassword(passwordEncoder.encode(updatePasswordRequest.newPassword()));
    userRepository.save(user);

    log.info("Password successfully updated for User with id : {}", user.getId());
  }

}
