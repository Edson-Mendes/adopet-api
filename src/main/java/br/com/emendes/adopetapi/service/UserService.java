package br.com.emendes.adopetapi.service;

import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;

/**
 * Interface Service que contém as abstrações que manipulam o recurso User.
 */
public interface UserService {


  /**
   * Atualiza o password de um User.
   * @param updatePasswordRequest contendo a senha antiga e a nova senha e confirmação da nova senha.
   */
  void updatePassword(UpdatePasswordRequest updatePasswordRequest);

}
