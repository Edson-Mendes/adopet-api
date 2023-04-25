package br.com.emendes.adopetapi.integration.shelter;

import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
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

import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_MANY_SHELTERS_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_SQL_PATH;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for PUT /api/shelters/{id}")
class UpdateShelterIT {

  @Autowired
  private WebTestClient webTestClient;

  private SignIn signIn;

  private static final String SHELTER_URI_TEMPLATE = "/api/shelters/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/shelters/{id} must return status 200 and ShelterResponse when update successfully")
  void putApiSheltersId_MustReturnStatus200AndShelterResponse_WhenUpdateSuccessfully() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateShelterRequest requestBody = UpdateShelterRequest.builder()
        .name("ONG Animal Shelter")
        .email("animal.shelter@email.com")
        .build();

    ShelterResponse actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateShelterRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(ShelterResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("ONG Animal Shelter");
    Assertions.assertThat(actualResponseBody.email()).isNotNull().isEqualTo("animal.shelter@email.com");
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/shelters/{id} must return status 400 and ProblemDetail when id is invalid")
  void putApiSheltersId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateShelterRequest requestBody = UpdateShelterRequest.builder()
        .name("ONG Animal Shelter")
        .email("animal.shelter@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1o0"))
        .header("Authorization", authorizationHeaderValue)
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateShelterRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Type mismatch");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("An error occurred trying to cast String to Number");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/shelters/1o0");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/shelters/{id} must return status 400 and ProblemDetail when request body has invalid fields")
  void putApiSheltersId_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateShelterRequest requestBody = UpdateShelterRequest.builder()
        .name("")
        .email("animalshelteremail.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateShelterRequest.class)
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
  @Sql(scripts = {INSERT_MANY_SHELTERS_SQL_PATH})
  @DisplayName("PUT /api/shelters/{id} must return status 400 and ProblemDetail when email already exists")
  void putApiSheltersId_MustReturnStatus400AndProblemDetail_WhenEmailAlreadyExists() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateShelterRequest requestBody = UpdateShelterRequest.builder()
        .name("ONG Animal Shelter")
        .email("shelterpoa@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateShelterRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Email already in use");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("E-mail {shelterpoa@email.com} is already in use");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/shelters/1");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_SQL_PATH})
  @DisplayName("PUT /api/shelters/{id} must return status 404 and ProblemDetail when a shelter tries to update another shelter")
  void putApiSheltersId_MustReturnStatus404AndProblemDetail_WhenAShelterTriesTOUpdateAnotherShelter() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateShelterRequest requestBody = UpdateShelterRequest.builder()
        .name("ONG Animal Shelter")
        .email("animal.shelter@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("2"))
        .header("Authorization", authorizationHeaderValue)
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateShelterRequest.class)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Shelter not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Shelter not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/shelters/2");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/shelters/{id} must return status 404 and ProblemDetail when shelter not found")
  void putApiSheltersId_MustReturnStatus404AndProblemDetail_WhenShelterNotFound() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateShelterRequest requestBody = UpdateShelterRequest.builder()
        .name("ONG Animal Shelter")
        .email("animal.shelter@email.com")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("100"))
        .header("Authorization", authorizationHeaderValue)
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateShelterRequest.class)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Shelter not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Shelter not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/shelters/100");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @DisplayName("PUT /api/shelters/{id} must return status 401 when client do not send JWT")
  void putApiSheltersId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    UpdateShelterRequest requestBody = UpdateShelterRequest.builder()
        .name("ONG Animal Shelter")
        .email("animal.shelter@email.com")
        .build();

    webTestClient.put()
        .uri(generateUri("1"))
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateShelterRequest.class)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  private String generateUri(String resourceId) {
    return String.format(SHELTER_URI_TEMPLATE, resourceId);
  }

}
