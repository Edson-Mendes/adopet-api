package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.model.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

  User getCurrentUser();

  Authentication getAuthentication();

}
