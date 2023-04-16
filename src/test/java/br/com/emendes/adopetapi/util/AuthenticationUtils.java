package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;

public class AuthenticationUtils {

  public static AuthenticationResponse authenticationResponse() {
    return AuthenticationResponse.builder()
        .type("Bearer")
        .token("jwt1234")
        .build();
  }

}
