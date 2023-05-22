package br.com.emendes.adopetapi.integration.endpoint.user;

import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;
import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
import br.com.emendes.adopetapi.util.component.SignIn;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.*;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_SQL_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for PATCH /api/users/password")
class UpdatePasswordIT {

  @Autowired
  private WebTestClient webTestClient;
  private SignIn signIn;

  private static final String UPDATE_PASSWORD_URI = "/api/users/password";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("PATCH /api/users/password must return status 204 when update password successfully")
  void patchApiUsersPassword_MustReturnStatus204_WhenUpdatePasswordSuccessfully() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdatePasswordRequest requestBody = UpdatePasswordRequest.builder()
        .oldPassword("1234567890")
        .newPassword("1234567890abcd")
        .confirmPassword("1234567890abcd")
        .build();

    webTestClient.patch()
        .uri(UPDATE_PASSWORD_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange().expectStatus().isNoContent();
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("PATCH /api/users/password must return status 400 and ProblemDetail when request body has invalid fields")
  void patchApiUsersPassword_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdatePasswordRequest requestBody = UpdatePasswordRequest.builder()
        .oldPassword("")
        .newPassword("1234567890abcd")
        .confirmPassword("1234567890abcd")
        .build();

    ProblemDetail actualResponseBody = webTestClient.patch()
        .uri(UPDATE_PASSWORD_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Invalid arguments");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Some fields are invalid");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/users/password");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);

    String actualFields = (String) actualResponseBody.getProperties().get("fields");
    String actualMessages = (String) actualResponseBody.getProperties().get("messages");

    Assertions.assertThat(actualFields).isNotNull().contains("oldPassword");
    Assertions.assertThat(actualMessages).isNotNull().contains("oldPassword must not be blank");
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("PATCH /api/users/password must return status 400 and ProblemDetail when passwords do not match")
  void patchApiUsersPassword_MustReturnStatus400AndProblemDetail_WhenPasswordsDoNotMatch() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdatePasswordRequest requestBody = UpdatePasswordRequest.builder()
        .oldPassword("1234567890")
        .newPassword("1234567890abcd")
        .confirmPassword("1234567890ab")
        .build();

    ProblemDetail actualResponseBody = webTestClient.patch()
        .uri(UPDATE_PASSWORD_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Passwords do not match");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Passwords do not match");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/users/password");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("PATCH /api/users/password must return status 400 and ProblemDetail when oldPassword is wrong")
  void patchApiUsersPassword_MustReturnStatus400AndProblemDetail_WhenOldPasswordIsWrong() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdatePasswordRequest requestBody = UpdatePasswordRequest.builder()
        .oldPassword("123456789")
        .newPassword("1234567890abcd")
        .confirmPassword("1234567890abcd")
        .build();

    ProblemDetail actualResponseBody = webTestClient.patch()
        .uri(UPDATE_PASSWORD_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Wrong password");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Old password is wrong");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/users/password");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @DisplayName("PATCH /api/users/password must return status 401 when client do not send JWT")
  void patchApiUsersPassword_MustReturnStatus401_WhenClientDoNotSendJWT() {
    UpdatePasswordRequest requestBody = UpdatePasswordRequest.builder()
        .oldPassword("1234567890")
        .newPassword("1234567890abcd")
        .confirmPassword("1234567890abcd")
        .build();

    webTestClient.put()
        .uri(UPDATE_PASSWORD_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isUnauthorized();
  }

}
