package br.com.emendes.adopetapi.util.impl;

import br.com.emendes.adopetapi.exception.UserIsNotAuthenticateException;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UserIsNotAuthenticateException("User is not authenticate");
    }

    return (User) authentication.getPrincipal();
  }

}
