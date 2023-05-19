package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.exception.UserIsNotAuthenticateException;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Role;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.CurrentUserService;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_GUARDIAN_NAME;
import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_SHELTER_NAME;

/**
 * Implementação de {@link CurrentUserService}.
 */
@RequiredArgsConstructor
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

  private final AuthenticationFacade authenticationFacade;
  private final GuardianRepository guardianRepository;
  private final ShelterRepository shelterRepository;

  @Override
  public User getCurrentUser() {
    Authentication authentication = authenticationFacade.getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UserIsNotAuthenticateException("User is not authenticate");
    }

    return (User) authentication.getPrincipal();
  }

  @Override
  public Optional<Guardian> getCurrentUserAsGuardian() {
    User currentUser = getCurrentUser();

    if (isGuardian(currentUser)) {
      return guardianRepository.findByUser(currentUser);
    }

    return Optional.empty();
  }

  @Override
  public Optional<Shelter> getCurrentUserAsShelter() {
    User currentUser = getCurrentUser();

    if (isShelter(currentUser)) {
      return shelterRepository.findByUser(currentUser);
    }

    return Optional.empty();
  }

  /**
   * Verifica se o User atual é um Guardian.
   * @param currentUser Usuário atual.
   * @return True se o usuário atual é um Guardian, false caso contrário.
   */
  private boolean isGuardian(User currentUser) {
    List<String> roles = getRolesName(currentUser);

    return roles.contains(ROLE_GUARDIAN_NAME);
  }

  /**
   * Verifica se o User atual é um Shelter.
   * @param currentUser Usuário atual.
   * @return True se o usuário atual é um Shelter, false caso contrário.
   */
  private boolean isShelter(User currentUser) {
    List<String> roles = getRolesName(currentUser);

    return roles.contains(ROLE_SHELTER_NAME);
  }

  /**
   * Devolve uma lista dos nomes das Roles do usuário.
   * @param currentUser Usuário atual.
   * @return Lista com os nomes das Roles do usuário.
   */
  private List<String> getRolesName(User currentUser) {
    return currentUser.getRoles().stream().map(Role::getName).toList();
  }

}
