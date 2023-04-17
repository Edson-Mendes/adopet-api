package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.AuthenticationController;
import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;
import br.com.emendes.adopetapi.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.emendes.adopetapi.util.AuthenticationUtils.authenticationResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(
    controllers = {AuthenticationController.class},
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@ActiveProfiles("test")
@DisplayName("Unit tests for AuthenticationController")
class AuthenticationControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private AuthenticationService authenticationServiceMock;

  private static final String AUTHENTICATION_URI = "/api/auth";
  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Tests for authenticate endpoint")
  class AuthenticateEndpoint {

    @Test
    @DisplayName("Authenticate must return status 200 and AuthenticationResponse when sign in successfully")
    void authenticate_MustReturnStatus200AndAuthenticationResponse_WhenSignInSuccessfully() throws Exception {
      BDDMockito.when(authenticationServiceMock.authenticate(any(AuthenticationRequest.class)))
          .thenReturn(authenticationResponse());
      String requestBody = """
            {
              "email" : "lorem@email.com",
              "password" : "1234567890"
            }
          """;

      String actualContent = mockMvc.perform(post(AUTHENTICATION_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      AuthenticationResponse actualAuthenticationResponse = mapper.readValue(actualContent, AuthenticationResponse.class);

      Assertions.assertThat(actualAuthenticationResponse).isNotNull();
      Assertions.assertThat(actualAuthenticationResponse.getType()).isNotNull().isEqualTo("Bearer");
      Assertions.assertThat(actualAuthenticationResponse.getToken()).isNotNull().isEqualTo("jwt1234");
    }

    @Test
    @DisplayName("Authenticate must return status 400 and ProblemDetail when credentials are invalid")
    void authenticate_MustReturnStatus400AndProblemDetail_WhenCredentialsAreInvalid() throws Exception {
      BDDMockito.when(authenticationServiceMock.authenticate(any(AuthenticationRequest.class)))
          .thenThrow(new BadCredentialsException("Bad Credentials"));
      String requestBody = """
            {
              "email" : "lorem@email.com",
              "password" : "1234567890"
            }
          """;

      String actualContent = mockMvc.perform(post(AUTHENTICATION_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Bad Credentials");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull().isEqualTo("Bad Credentials");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Authenticate must return status 400 and ProblemDetail when request body has invalid fields")
    void authenticate_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "email" : "",
              "password" : "1234567890"
            }
          """;

      String actualContent = mockMvc.perform(post(AUTHENTICATION_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Invalid arguments");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Some fields are invalid");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);

      Assertions.assertThat(actualProblemDetail.getProperties()).isNotNull();

      String actualFields = (String) actualProblemDetail.getProperties().get("fields");
      String actualMessages = (String) actualProblemDetail.getProperties().get("messages");

      Assertions.assertThat(actualFields).isNotNull().contains("email");
      Assertions.assertThat(actualMessages).isNotNull().contains("email must not be blank");
    }

  }

}