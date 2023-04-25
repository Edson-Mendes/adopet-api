package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;

public class AuthenticationUtils {

  public static AuthenticationResponse authenticationResponse() {
    return AuthenticationResponse.builder()
        .type("Bearer")
        .token("jwt1234")
        .build();
  }

  public static AuthenticationRequest shelterAuthenticationRequest() {
    return AuthenticationRequest.builder()
        .email("animal.shelter@email.com")
        .password("1234567890")
        .build();
  }

  public static AuthenticationRequest guardianAuthenticationRequest() {
    return AuthenticationRequest.builder()
        .email("lorem@email.com")
        .password("1234567890")
        .build();
  }

}
