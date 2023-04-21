package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;

import java.util.Optional;

public interface UserService {

  User getCurrentUser();

  Optional<Guardian> getCurrentUserAsGuardian();

  Optional<Shelter> getCurrentUserAsShelter();

}
