package br.com.emendes.adopetapi.integration.adoption;

import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.repository.AdoptionRepository;
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
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_ADOPTIONS_MANY_SHELTERS_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for DELETE /api/adoptions/{id}")
class DeleteAdoptionIT {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private AdoptionRepository adoptionRepository;

  private SignIn signIn;

  private static final String GUARDIAN_URI_TEMPLATE = "/api/adoptions/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("DELETE /api/adoptions/{id} must return status 204 when delete successfully")
  void DeleteApiAdoptionsId_MustReturnStatus204_WhenDeleteSuccessfully() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    webTestClient.delete()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNoContent();

    Optional<Adoption> actualAdoptionOptional = adoptionRepository.findById(1L);

    Assertions.assertThat(actualAdoptionOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("DELETE /api/adoptions/{id} must return status 400 and ProblemDetail when id is invalid")
  void deleteApiAdoptionsId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());


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
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/1o0");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTIONS_MANY_SHELTERS_SQL_PATH})
  @DisplayName("DELETE /api/adoptions/{id} must return status 404 and ProblemDetail when adoption does not belong to current shelter")
  void deleteApiAdoptionsId_MustReturnStatus404AndProblemDetail_WhenAdoptionDoesNotBelongToCurrentShelter() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("2"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/2");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("DELETE /api/adoptions/{id} must return status 404 and ProblemDetail when adoption not found")
  void deleteApiAdoptionsId_MustReturnStatus404AndProblemDetail_WhenAdoptionNotFound() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("100"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/100");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @DisplayName("DELETE /api/adoptions/{id} must return status 401 when client do not send JWT")
  void deleteApiAdoptionsId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.delete()
        .uri(generateUri("1"))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTIONS_MANY_SHELTERS_SQL_PATH})
  @DisplayName("DELETE /api/adoptions/{id} must return status 403 when user is not a SHELTER")
  void deleteApiAdoptionsId_MustReturnStatus403_WhenUserIsNotAShelter() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

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
