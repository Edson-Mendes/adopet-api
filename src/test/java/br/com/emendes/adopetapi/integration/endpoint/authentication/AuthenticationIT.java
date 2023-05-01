package br.com.emendes.adopetapi.integration.endpoint.authentication;

import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ProblemDetail;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE_HEADER_NAME;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_SQL_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for POST /api/auth")
class AuthenticationIT {

  @Autowired
  private WebTestClient webTestClient;

  private static final String AUTH_URI = "/api/auth";

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("POST /api/auth must return status 200 and AuthenticationResponse when guardian authenticate successfully")
  void postApiAuth_MustReturnStatus200AndAuthenticationResponse_WhenGuardianAuthenticateSuccessfully() {
    AuthenticationRequest requestBody = AuthenticationRequest.builder()
        .email("lorem@email.com")
        .password("1234567890")
        .build();

    AuthenticationResponse actualResponseBody = webTestClient.post()
        .uri(AUTH_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), AuthenticationRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AuthenticationResponse.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.token()).isNotNull();
    Assertions.assertThat(actualResponseBody.type()).isNotNull().isEqualTo("Bearer");
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("POST /api/auth must return status 200 and AuthenticationResponse when shelter authenticate successfully")
  void postApiAuth_MustReturnStatus200AndAuthenticationResponse_WhenShelterAuthenticateSuccessfully() {
    AuthenticationRequest requestBody = AuthenticationRequest.builder()
        .email("animal.shelter@email.com")
        .password("1234567890")
        .build();

    AuthenticationResponse actualResponseBody = webTestClient.post()
        .uri(AUTH_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), AuthenticationRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AuthenticationResponse.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.token()).isNotNull();
    Assertions.assertThat(actualResponseBody.type()).isNotNull().isEqualTo("Bearer");
  }

  @Test
  @DisplayName("POST /api/auth must return status 400 and ProblemDetail when send invalid credentials")
  void postApiAuth_MustReturnStatus400AndProblemDetail_WhenSendInvalidCredentials() {
    AuthenticationRequest requestBody = AuthenticationRequest.builder()
        .email("animal.shelter@email.com")
        .password("1234567890")
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(AUTH_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), AuthenticationRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Bad credentials");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Incorrect email or password");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull();
  }

  @Test
  @DisplayName("POST /api/guardians must return status 400 and ProblemDetail when request body has invalid fields")
  void postApiGuardians_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    AuthenticationRequest requestBody = AuthenticationRequest.builder()
        .email("animalshelteremailcom")
        .password("")
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(AUTH_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), CreateGuardianRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Invalid arguments");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Some fields are invalid");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);

    Assertions.assertThat(actualResponseBody.getProperties()).isNotNull();

    String actualFields = (String) actualResponseBody.getProperties().get("fields");
    String actualMessages = (String) actualResponseBody.getProperties().get("messages");

    Assertions.assertThat(actualFields).isNotNull().contains("email", "password");
    Assertions.assertThat(actualMessages).isNotNull().contains("must be a well formed email", "password must not be blank");
  }

}
