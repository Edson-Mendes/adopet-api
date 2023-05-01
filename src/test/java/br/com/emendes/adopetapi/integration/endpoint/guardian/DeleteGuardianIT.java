package br.com.emendes.adopetapi.integration.endpoint.guardian;

import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.util.component.SignIn;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Optional;

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.AUTHORIZATION_HEADER_NAME;
import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for DELETE /api/guardians/{id}")
class DeleteGuardianIT {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private GuardianRepository guardianRepository;

  private SignIn signIn;

  private static final String GUARDIAN_URI_TEMPLATE = "/api/guardians/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("DELETE /api/guardians/{id} must return status 204 when delete successfully")
  void DeleteApiGuardiansId_MustReturnStatus204_WhenDeleteSuccessfully() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    webTestClient.delete()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNoContent();

    Optional<Guardian> actualGuardianOptional = guardianRepository.findById(1L);

    Assertions.assertThat(actualGuardianOptional).isPresent();
    Guardian actualGuardian = actualGuardianOptional.get();

    Assertions.assertThat(actualGuardian).isNotNull();
    Assertions.assertThat(actualGuardian.isDeleted()).isTrue();
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("DELETE /api/guardians/{id} must return status 400 and ProblemDetail when id is invalid")
  void deleteApiGuardiansId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());


    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("1o0"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Type mismatch");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("An error occurred trying to cast String to Number");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/guardians/1o0");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_MANY_GUARDIANS_SQL_PATH})
  @DisplayName("DELETE /api/guardians/{id} must return status 404 and ProblemDetail when a guardian tries to delete another guardian")
  void deleteApiGuardiansId_MustReturnStatus404AndProblemDetail_WhenAGuardianTriesToDeleteAnotherGuardian() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("2"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Guardian not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Guardian not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/guardians/2");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("DELETE /api/guardians/{id} must return status 404 and ProblemDetail when guardian not found")
  void deleteApiGuardiansId_MustReturnStatus404AndProblemDetail_WhenGuardianNotFound() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("100"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Guardian not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Guardian not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/guardians/100");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @DisplayName("DELETE /api/guardians/{id} must return status 401 when client do not send JWT")
  void deleteApiGuardiansId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.delete()
        .uri(generateUri("1"))
        .header("Content-Type", CONTENT_TYPE)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("DELETE /api/guardians/{id} must return status 403 when user is not a GUARDIAN")
  void deleteApiGuardiansId_MustReturnStatus403_WhenUserIsNotAGuardian() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    webTestClient.delete()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isForbidden();
  }

  private String generateUri(String resourceId) {
    return String.format(GUARDIAN_URI_TEMPLATE, resourceId);
  }

}
