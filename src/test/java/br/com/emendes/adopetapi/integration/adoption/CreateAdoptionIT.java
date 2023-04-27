package br.com.emendes.adopetapi.integration.adoption;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
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
import reactor.core.publisher.Mono;

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.*;
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for POST /api/adoptions")
class CreateAdoptionIT {

  @Autowired
  private WebTestClient webTestClient;
  private SignIn signIn;

  private static final String ADOPTION_URI = "/api/adoptions";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_SQL_PATH})
  @DisplayName("POST /api/adoptions must return status 201 and AdoptionResponse when adopt successfully")
  void postApiAdoptions_MustReturnStatus201AndAdoptionResponse_WhenAdoptSuccessfully() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    AdoptionRequest requestBody = AdoptionRequest.builder()
        .petId(1L)
        .build();

    AdoptionResponse actualResponseBody = webTestClient.post()
        .uri(ADOPTION_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), AdoptionRequest.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(AdoptionResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.date()).isNotNull();
    Assertions.assertThat(actualResponseBody.petId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.guardianId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.status()).isNotNull().isEqualByComparingTo(AdoptionStatus.ANALYSING);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_SQL_PATH})
  @DisplayName("POST /api/adoptions must return status 400 and ProblemDetail when pet does not exist")
  void postApiAdoptions_MustReturnStatus400AndProblemDetail_WhenPetDoesNotExist() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    AdoptionRequest requestBody = AdoptionRequest.builder()
        .petId(100L)
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(ADOPTION_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), AdoptionRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Something went wrong");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Invalid pet id");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTED_PET_SHELTER_GUARDIAN_SQL_PATH})
  @DisplayName("POST /api/adoptions must return status 400 and ProblemDetail when pet has already been adopted")
  void postApiAdoptions_MustReturnStatus400AndProblemDetail_WhenPetHasAlreadyBeenAdopted() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    AdoptionRequest requestBody = AdoptionRequest.builder()
        .petId(1L)
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(ADOPTION_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), AdoptionRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Something went wrong");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Invalid pet id");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_PET_DELETED_SHELTER_GUARDIAN_SQL_PATH})
  @DisplayName("POST /api/adoptions must return status 400 and ProblemDetail when pet belong to a deleted shelter")
  void postApiAdoptions_MustReturnStatus400AndProblemDetail_WhenPetBelongToADeletedShelter() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    AdoptionRequest requestBody = AdoptionRequest.builder()
        .petId(1L)
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(ADOPTION_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), AdoptionRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Something went wrong");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Invalid pet id");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("POST /api/adoptions must return status 400 and ProblemDetail when request body has invalid fields")
  void postApiAdoptions_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    AdoptionRequest requestBody = AdoptionRequest.builder()
        .petId(null)
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(ADOPTION_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), AdoptionRequest.class)
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

    Assertions.assertThat(actualFields).isNotNull().contains("petId");
    Assertions.assertThat(actualMessages).isNotNull().contains("petId must not be null");
  }

  @Test
  @DisplayName("POST /api/adoptions must return status 401 when client do not send JWT")
  void postApiAdoptions_MustReturnStatus401_WhenClientDoNotSendJWT() {
    AdoptionRequest requestBody = AdoptionRequest.builder()
        .petId(1L)
        .build();

    webTestClient.post()
        .uri(ADOPTION_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), AdoptionRequest.class)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("POST /api/adoptions must return status 403 when user is not a GUARDIAN")
  void postApiAdoptions_MustReturnStatus403_WhenUserIsNotAGuardian() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    AdoptionRequest requestBody = AdoptionRequest.builder()
        .petId(1L)
        .build();

    webTestClient.post()
        .uri(ADOPTION_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), AdoptionRequest.class)
        .exchange()
        .expectStatus().isForbidden();
  }

}
