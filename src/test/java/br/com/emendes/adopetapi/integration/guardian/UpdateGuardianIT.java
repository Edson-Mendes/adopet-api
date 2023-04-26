package br.com.emendes.adopetapi.integration.guardian;

import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
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
import reactor.core.publisher.Mono;

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE_HEADER_NAME;
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for PUT /api/guardians/{id}")
class UpdateGuardianIT {

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
  @DisplayName("PUT /api/guardians/{id} must return status 200 and GuardianResponse when update successfully")
  void putApiGuardiansId_MustReturnStatus200AndGuardianResponse_WhenUpdateSuccessfully() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("Lorem X Ipsum")
        .email("loremipsum@email.com")
        .build();

    GuardianResponse actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(GuardianResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("Lorem X Ipsum");
    Assertions.assertThat(actualResponseBody.email()).isNotNull().isEqualTo("loremipsum@email.com");
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("PUT /api/guardians/{id} must return status 400 and ProblemDetail when id is invalid")
  void putApiGuardiansId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("Lorem X Ipsum")
        .email("loremipsum@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1o0"))
        .header("Authorization", authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
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
  @DisplayName("PUT /api/guardians/{id} must return status 400 and ProblemDetail when request body has invalid fields")
  void putApiGuardiansId_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("")
        .email("loremipsumemailcom")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Invalid arguments");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Some fields are invalid");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);

    Assertions.assertThat(actualResponseBody.getProperties()).isNotNull();

    String actualFields = (String) actualResponseBody.getProperties().get("fields");
    String actualMessages = (String) actualResponseBody.getProperties().get("messages");

    Assertions.assertThat(actualFields).isNotNull().contains("name", "email");
    Assertions.assertThat(actualMessages).isNotNull().contains("name must not be blank", "must be a well formed email");
  }

  @Test
  @Sql(scripts = {INSERT_MANY_GUARDIANS_SQL_PATH})
  @DisplayName("PUT /api/guardians/{id} must return status 400 and ProblemDetail when email already exists")
  void putApiGuardiansId_MustReturnStatus400AndProblemDetail_WhenEmailAlreadyExists() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("Lorem X Ipsum")
        .email("dolor@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Email already in use");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("E-mail {dolor@email.com} is already in use");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/guardians/1");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_MANY_GUARDIANS_SQL_PATH})
  @DisplayName("PUT /api/guardians/{id} must return status 404 and ProblemDetail when a guardian tries to update another guardian")
  void putApiGuardiansId_MustReturnStatus404AndProblemDetail_WhenAGuardianTriesToUpdateAnotherGuardian() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("Lorem X Ipsum")
        .email("loremipsum@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("2"))
        .header("Authorization", authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
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
  @DisplayName("PUT /api/guardians/{id} must return status 404 and ProblemDetail when guardian not found")
  void putApiGuardiansId_MustReturnStatus404AndProblemDetail_WhenGuardianNotFound() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());
    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("Lorem X Ipsum")
        .email("loremipsum@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("100"))
        .header("Authorization", authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
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
  @DisplayName("PUT /api/guardians/{id} must return status 401 when client do not send JWT")
  void putApiGuardiansId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("Lorem X Ipsum")
        .email("loremipsum@email.com")
        .build();

    webTestClient.put()
        .uri(generateUri("1"))
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/guardians/{id} must return status 403 when user not is a GUARDIAN")
  void putApiGuardiansId_MustReturnStatus403_WhenUserNotIsAGuardian() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    UpdateGuardianRequest requestBody = UpdateGuardianRequest.builder()
        .name("Lorem X Ipsum")
        .email("loremipsum@email.com")
        .build();

    webTestClient.put()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateGuardianRequest.class)
        .exchange()
        .expectStatus().isForbidden();
  }

  private String generateUri(String resourceId) {
    return String.format(GUARDIAN_URI_TEMPLATE, resourceId);
  }

}
