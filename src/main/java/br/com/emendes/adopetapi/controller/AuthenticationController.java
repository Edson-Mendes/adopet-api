package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.controller.swagger.AuthenticationControllerSwagger;
import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;
import br.com.emendes.adopetapi.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController implements AuthenticationControllerSwagger {

  private final AuthenticationService authenticationService;

  @PostMapping
  public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authRequest) {
    return ResponseEntity.ok(authenticationService.authenticate(authRequest));
  }

}
