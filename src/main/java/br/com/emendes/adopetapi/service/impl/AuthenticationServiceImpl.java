package br.com.emendes.adopetapi.service.impl;

import br.com.emendes.adopetapi.config.security.service.JwtService;
import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;
import br.com.emendes.adopetapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

    UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

    String token = jwtService.generateToken(userDetails);

    log.info("token generate successfully for user : {}", userDetails.getUsername());

    return AuthenticationResponse.builder()
        .type("Bearer")
        .token(token)
        .build();
  }

}
