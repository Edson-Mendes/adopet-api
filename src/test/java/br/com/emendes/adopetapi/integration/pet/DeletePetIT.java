package br.com.emendes.adopetapi.integration.pet;

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

import java.util.Optional;

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.AUTHORIZATION_HEADER_NAME;
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for DELETE /api/pets/{id}")
class DeletePetIT {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private PetRepository petRepository;

  private SignIn signIn;

  private static final String PET_URI_TEMPLATE = "/api/pets/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_SQL_PATH})
  @DisplayName("DELETE /api/pets/{id} must return status 204 when delete successfully")
  void DeleteApiPetsId_MustReturnStatus204_WhenDeleteSuccessfully() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    webTestClient.delete()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNoContent();

    Optional<Pet> actualPetOptional = petRepository.findById(1L);

    Assertions.assertThat(actualPetOptional).isNotPresent();
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_SQL_PATH})
  @DisplayName("DELETE /api/pets/{id} must return status 400 and ProblemDetail when id is invalid")
  void deleteApiPetsId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
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
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/pets/1o0");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH})
  @DisplayName("DELETE /api/pets/{id} must return status 400 and ProblemDetail when pet is on adoption process")
  void deleteApiPetsId_MustReturnStatus400AndProblemDetail_WhenPetIsOnAdoptionPrecess() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());


    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Something went wrong");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("This pet cannot be deleted because it is in process of being adopted.");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/pets/1");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_AND_PETS_SQL_PATH})
  @DisplayName("DELETE /api/pets/{id} must return status 404 and ProblemDetail when a shelter tries to delete a pet that does not belong to him")
  void deleteApiPetsId_MustReturnStatus404AndProblemDetail_WhenAShelterTriesToDeleteAPetThatDoesNotBelongToHim() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.delete()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ProblemDetail.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Pet not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Pet not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/pets/1");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_SQL_PATH})
  @DisplayName("DELETE /api/pets/{id} must return status 404 and ProblemDetail when pet not found")
  void deleteApiPetsId_MustReturnStatus404AndProblemDetail_WhenPetNotFound() {
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
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Pet not found");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Pet not found");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/pets/100");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(404);
  }

  @Test
  @DisplayName("DELETE /api/pets/{id} must return status 401 when client do not send JWT")
  void deleteApiPetsId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.delete()
        .uri(generateUri("1"))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("DELETE /api/pets/{id} must return status 403 when user not is a SHELTER")
  void deleteApiPetsId_MustReturnStatus403_WhenUserNotIsAShelter() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    webTestClient.delete()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isForbidden();
  }

  private String generateUri(String resourceId) {
    return String.format(PET_URI_TEMPLATE, resourceId);
  }

}
