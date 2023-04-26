package br.com.emendes.adopetapi.integration.guardian;

import br.com.emendes.adopetapi.dto.response.GuardianResponse;
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

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_AND_SHELTER_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_SQL_PATH;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for GET /api/guardians/{id}")
class FindByIdGuardianIT {

  @Autowired
  private WebTestClient webTestClient;

  private SignIn signIn;

  private static final String GUARDIAN_URI_TEMPLATE = "/api/guardians/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/guardians/{id} must return status 200 and GuardianResponse when find by id successfully")
  void getApiGuardiansId_MustReturnStatus200AndGuardianResponse_WhenFindByIdSuccessfully() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    GuardianResponse actualResponseBody = webTestClient.get()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(GuardianResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualResponseBody.email()).isNotNull().isEqualTo("lorem@email.com");
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_AND_SHELTER_SQL_PATH})
  @DisplayName("GET /api/guardians/{id} must return status 200 and GuardianResponse when shelter user find by id successfully")
  void getApiGuardiansId_MustReturnStatus200AndGuardianResponse_WhenShelterUserFindByIdSuccessfully() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    GuardianResponse actualResponseBody = webTestClient.get()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(GuardianResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualResponseBody.email()).isNotNull().isEqualTo("lorem@email.com");
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/guardians/{id} must return status 400 and ProblemDetail when id is invalid")
  void getApiGuardiansId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.get()
        .uri(generateUri("1o0"))
        .header("Authorization", authorizationHeaderValue)
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
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/guardians/{id} must return status 400 and ProblemDetail when guardian not found")
  void getApiGuardiansId_MustReturnStatus400AndProblemDetail_WhenGuardianNotFound() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.get()
        .uri(generateUri("100"))
        .header("Authorization", authorizationHeaderValue)
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
  @DisplayName("GET /api/guardians must return status 401 when client do not send JWT")
  void getApiGuardians_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.get()
        .uri(generateUri("1"))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  private String generateUri(String resourceId) {
    return String.format(GUARDIAN_URI_TEMPLATE, resourceId);
  }

}
