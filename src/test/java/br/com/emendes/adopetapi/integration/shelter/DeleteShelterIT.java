package br.com.emendes.adopetapi.integration.shelter;

import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.ShelterRepository;
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
import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_MANY_SHELTERS_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_SQL_PATH;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for DELETE /api/shelters/{id}")
class DeleteShelterIT {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private ShelterRepository shelterRepository;

  private SignIn signIn;

  private static final String SHELTER_URI_TEMPLATE = "/api/shelters/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("DELETE /api/shelters/{id} must return status 204 when delete successfully")
  void DeleteApiSheltersId_MustReturnStatus204_WhenDeleteSuccessfully() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    webTestClient.delete()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .exchange()
        .expectStatus().isNoContent();

    Optional<Shelter> actualShelterOptional = shelterRepository.findById(1L);

    Assertions.assertThat(actualShelterOptional).isPresent();
    Shelter actualShelter = actualShelterOptional.get();

    Assertions.assertThat(actualShelter).isNotNull();
    Assertions.assertThat(actualShelter.isDeleted()).isTrue();
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("DELETE /api/shelters/{id} must return status 400 and ProblemDetail when id is invalid")
  void deleteApiSheltersId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());


    ProblemDetail actualResponseBody = webTestClient.delete()
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
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/shelters/1o0");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_SQL_PATH})
  @DisplayName("DELETE /api/shelters/{id} must return status 404 and ProblemDetail when a shelter tries to delete another shelter")
  void deleteApiSheltersId_MustReturnStatus404AndProblemDetail_WhenAShelterTriesToDeleteAnotherShelter() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("2"))
        .header("Authorization", authorizationHeaderValue)
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
  @DisplayName("DELETE /api/shelters/{id} must return status 404 and ProblemDetail when shelter not found")
  void deleteApiSheltersId_MustReturnStatus404AndProblemDetail_WhenShelterNotFound() {
    // Realizar Login antes de buscar shelters.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("100"))
        .header("Authorization", authorizationHeaderValue)
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
  @DisplayName("DELETE /api/shelters/{id} must return status 401 when client do not send JWT")
  void deleteApiSheltersId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.delete()
        .uri(generateUri("1"))
        .header("Content-Type", CONTENT_TYPE)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("DELETE /api/shelters/{id} must return status 403 when user not is a SHELTER")
  void deleteApiSheltersId_MustReturnStatus403_WhenUserNotIsAShelter() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    webTestClient.delete()
        .uri(generateUri("1"))
        .header("Authorization", authorizationHeaderValue)
        .exchange()
        .expectStatus().isForbidden();
  }

  private String generateUri(String resourceId) {
    return String.format(SHELTER_URI_TEMPLATE, resourceId);
  }

}
