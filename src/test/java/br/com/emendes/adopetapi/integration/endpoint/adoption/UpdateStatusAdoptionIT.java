package br.com.emendes.adopetapi.integration.endpoint.adoption;

import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.repository.PetRepository;
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
import static br.com.emendes.adopetapi.util.ConstantUtils.*;
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for PUT /api/adoptions/{id}/status")
class UpdateStatusAdoptionIT {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private PetRepository petRepository;

  private SignIn signIn;

  private static final String ADOPTION_URI_TEMPLATE = "/api/adoptions/%s/status";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 200 and AdoptionResponse when update successfully")
  void putApiAdoptionsIdStatus_MustReturnStatus200AndAdoptionResponse_WhenUpdateSuccessfully() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDED")
        .build();

    AdoptionResponse actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AdoptionResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.date()).isNotNull();
    Assertions.assertThat(actualResponseBody.petId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.guardianId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.status()).isNotNull().isEqualByComparingTo(AdoptionStatus.CONCLUDED);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 200 and Pet.adopted = true when update status to concluded")
  void putApiAdoptionsIdStatus_MustReturnStatus200AndPetAdoptedEqualTrue_WhenUpdateStatusToConcluded() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDED")
        .build();

    AdoptionResponse actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AdoptionResponse.class)
        .returnResult().getResponseBody();


    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.date()).isNotNull();
    Assertions.assertThat(actualResponseBody.petId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.guardianId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.status()).isNotNull().isEqualByComparingTo(AdoptionStatus.CONCLUDED);

    Pet actualPet = petRepository.findById(1L).orElseThrow();

    Assertions.assertThat(actualPet.isAdopted()).isTrue();
  }

  @Test
  @Sql(scripts = {INSERT_CONCLUDED_ADOPTION_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 200 and Pet.adopted = false when update status to canceled")
  void putApiAdoptionsIdStatus_MustReturnStatus200AndPetAdoptedEqualFalse_WhenUpdateStatusToCanceled() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CANCELED")
        .build();

    AdoptionResponse actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AdoptionResponse.class)
        .returnResult().getResponseBody();


    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.date()).isNotNull();
    Assertions.assertThat(actualResponseBody.petId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.guardianId()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualResponseBody.status()).isNotNull().isEqualByComparingTo(AdoptionStatus.CANCELED);

    Pet actualPet = petRepository.findById(1L).orElseThrow();

    Assertions.assertThat(actualPet.isAdopted()).isFalse();
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 400 and ProblemDetail when id is invalid")
  void putApiAdoptionsIdStatus_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDED")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1o0"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Type mismatch");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("An error occurred trying to cast String to Number");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/1o0/status");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 400 and ProblemDetail when request body has invalid fields")
  void putApiAdoptionsIdStatus_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDEDDD")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
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
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);

    Assertions.assertThat(actualResponseBody.getProperties()).isNotNull();

    String actualFields = (String) actualResponseBody.getProperties().get("fields");
    String actualMessages = (String) actualResponseBody.getProperties().get("messages");

    Assertions.assertThat(actualFields).isNotNull().contains("status");
    Assertions.assertThat(actualMessages).isNotNull().contains("must be a valid status");
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 404 and ProblemDetail when adoption not found")
  void putApiAdoptionsIdStatus_MustReturnStatus404AndProblemDetail_WhenAdoptionNotFound() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDED")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("100"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/100/status");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTIONS_MANY_SHELTERS_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 404 and ProblemDetail when adoption does not belong to current shelter")
  void putApiAdoptionsIdStatus_MustReturnStatus404AndProblemDetail_WhenAdoptionDoesNotBelongToCurrentShelter() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDED")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("3"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Adoption not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/adoptions/3/status");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @DisplayName("PUT /api/adoptions/{id}/status must return status 401 when client do not send JWT")
  void putApiAdoptionsIdStatus_MustReturnStatus401_WhenClientDoNotSendJWT() {
    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDED")
        .build();

    webTestClient.put()
        .uri(generateUri("1"))
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("PUT /api/adoptions/{id}/status must return status 403 when user is not a SHELTER")
  void putApiAdoptionsIdStatus_MustReturnStatus403_WhenUserIsNotAShelter() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    UpdateStatusRequest requestBody = UpdateStatusRequest.builder()
        .status("CONCLUDED")
        .build();

    webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdateStatusRequest.class)
        .exchange()
        .expectStatus().isForbidden();
  }

  private String generateUri(String resourceId) {
    return String.format(ADOPTION_URI_TEMPLATE, resourceId);
  }

}
