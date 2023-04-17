package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.model.entity.User;

public interface AuthenticationFacade {

  User getCurrentUser();

}
