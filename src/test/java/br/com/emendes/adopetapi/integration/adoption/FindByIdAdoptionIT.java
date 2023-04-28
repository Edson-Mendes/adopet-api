package br.com.emendes.adopetapi.integration.adoption;

import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.model.AdoptionStatus;
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

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.*;
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for GET /api/adoptions/{id}")
class FindByIdAdoptionIT {

  @Autowired
  private WebTestClient webTestClient;
  private SignIn signIn;

  private static final String GUARDIAN_URI_TEMPLATE = "/api/adoptions/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("GET /api/adoptions/{id} must return status 200 and AdoptionResponse when adoption belongs to current guardian user")
  void getApiAdoptionsId_MustReturnStatus200AndAdoptionResponse_WhenAdoptionBelongsToCurrentGuardianUser() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    AdoptionResponse actualResponseBody = webTestClient.get()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AdoptionResponse.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.status()).isNotNull().isEqualByComparingTo(AdoptionStatus.ANALYSING);
    Assertions.assertThat(actualResponseBody.petId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.guardianId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.date()).isNotNull();
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("GET /api/adoptions/{id} must return status 200 and AdoptionResponse when adoption belongs to current shelter user")
  void getApiAdoptionsId_MustReturnStatus200AndAdoptionResponse_WhenAdoptionBelongsToCurrentShelterUser() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    AdoptionResponse actualResponseBody = webTestClient.get()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AdoptionResponse.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.status()).isNotNull().isEqualByComparingTo(AdoptionStatus.ANALYSING);
    Assertions.assertThat(actualResponseBody.petId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.guardianId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.date()).isNotNull();
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("GET /api/adoptions/{id} must return status 400 and ProblemDetail when id is invalid")
  void getApiAdoptionsId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.get()
        .uri(generateUri("1o0"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Type mismatch");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("An error occurred trying to cast String to Number");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/1o0");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTIONS_MANY_GUARDIANS_SQL_PATH})
  @DisplayName("GET /api/adoptions/{id} must return status 404 and ProblemDetail when adoption does not belong to current guardian user")
  void getApiAdoptionsId_MustReturnStatus404AndProblemDetail_WhenAdoptionDoesNotBelongToCurrentGuardianUser() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.get()
        .uri(generateUri("2"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/2");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTIONS_MANY_SHELTERS_SQL_PATH})
  @DisplayName("GET /api/adoptions/{id} must return status 404 and ProblemDetail when adoption does not belong to current shelter user")
  void getApiAdoptionsId_MustReturnStatus404AndProblemDetail_WhenAdoptionDoesNotBelongToCurrentShelterUser() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.get()
        .uri(generateUri("2"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/2");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @DisplayName("GET /api/adoptions/{id} must return status 401 when client do not send JWT")
  void getApiAdoptionsId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.get()
        .uri(generateUri("1"))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  private String generateUri(String resourceId) {
    return String.format(GUARDIAN_URI_TEMPLATE, resourceId);
  }

}
