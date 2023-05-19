package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;

import java.util.Optional;

/**
 * Interface Service que contém as abstrações que interagem com o Current User.
 */
public interface CurrentUserService {

  /**
   * Retorna o usuário atual.
   *
   * @return User atual.
   */
  User getCurrentUser();

  /**
   * Se o usuário atual for um Guardian, devolve um Optional contendo o Guardian associado ao User atual.
   */
  Optional<Guardian> getCurrentUserAsGuardian();

  /**
   * Se o usuário atual for um Shelter, devolve um Optional contendo o Shelter associado ao User atual.
   */
  Optional<Shelter> getCurrentUserAsShelter();

}
