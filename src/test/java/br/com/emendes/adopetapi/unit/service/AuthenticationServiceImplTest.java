package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.config.security.service.JwtService;
import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;
import br.com.emendes.adopetapi.service.impl.AuthenticationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.emendes.adopetapi.util.UserUtils.guardianUser;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for AuthenticationServiceImpl")
class AuthenticationServiceImplTest {

  @InjectMocks
  private AuthenticationServiceImpl authenticationService;
  @Mock
  private JwtService jwtServiceMock;
  @Mock
  private AuthenticationManager authenticationManager;

  @Nested
  @DisplayName("Tests for authenticate method")
  class AuthenticateMethod {

    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("Authenticate must return AuthenticationResponse when authenticate successfully")
    void authenticate_MustReturnAuthenticationResponse_WhenAuthenticateSuccessfully() {
      BDDMockito.when(authenticationManager.authenticate(any(Authentication.class)))
          .thenReturn(authentication);
      BDDMockito.when(authentication.getPrincipal()).thenReturn(guardianUser());
      BDDMockito.when(jwtServiceMock.generateToken(any(UserDetails.class)))
          .thenReturn("jwt1234");

      AuthenticationRequest authRequest = AuthenticationRequest.builder()
          .email("lorem@email.com")
          .password("1234567890")
          .build();

      AuthenticationResponse actualAuthenticationResponse = authenticationService.authenticate(authRequest);

      Assertions.assertThat(actualAuthenticationResponse).isNotNull();
      Assertions.assertThat(actualAuthenticationResponse.getType()).isNotNull().isEqualTo("Bearer");
      Assertions.assertThat(actualAuthenticationResponse.getToken()).isNotNull().isEqualTo("jwt1234");
    }

    @Test
    @DisplayName("Authenticate must throw BadCredentialsException when credentials are invalid")
    void authenticate_MustThrowBadCredentialsException_WhenCredentialsAreInvalid() {
      BDDMockito.when(authenticationManager.authenticate(any(Authentication.class)))
          .thenThrow(new BadCredentialsException("Bad Credentials"));

      AuthenticationRequest authRequest = AuthenticationRequest.builder()
          .email("lorem@email.com")
          .password("1234567890")
          .build();

      Assertions.assertThatExceptionOfType(BadCredentialsException.class)
          .isThrownBy(() -> authenticationService.authenticate(authRequest))
          .withMessageContaining("Bad Credentials");
    }

  }

}