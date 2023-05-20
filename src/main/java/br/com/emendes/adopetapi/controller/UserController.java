package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.controller.swagger.UserControllerSwagger;
import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;
import br.com.emendes.adopetapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller que recebe as requisições que tratam do recurso User.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerSwagger {

  private final UserService userService;

  /**
   * Trata a requisição PATCH /api/users<br>
   * É necessário estar autenticado.
   */
  @PatchMapping("/password")
  public ResponseEntity<Void> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
    userService.updatePassword(updatePasswordRequest);
    return ResponseEntity.noContent().build();
  }

}
