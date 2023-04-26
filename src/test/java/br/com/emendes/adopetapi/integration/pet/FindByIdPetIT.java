package br.com.emendes.adopetapi.integration.pet;

import br.com.emendes.adopetapi.dto.response.PetResponse;
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
import static br.com.emendes.adopetapi.util.ConstantUtils.AUTHORIZATION_HEADER_NAME;
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for GET /api/pets/{id}")
class FindByIdPetIT {

  @Autowired
  private WebTestClient webTestClient;

  private SignIn signIn;

  private static final String GUARDIAN_URI_TEMPLATE = "/api/pets/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/pets/{id} must return status 200 and PetResponse when guardian user find by id successfully")
  void getApiPetsId_MustReturnStatus200AndPetResponse_WhenGuardianUserFindByIdSuccessfully() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    PetResponse actualResponseBody = webTestClient.get()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PetResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualResponseBody.description()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualResponseBody.age()).isNotNull().isEqualTo("2 years old");
    Assertions.assertThat(actualResponseBody.adopted()).isFalse();
    Assertions.assertThat(actualResponseBody.images()).isNotNull().hasSize(1);
    Assertions.assertThat(actualResponseBody.shelterId()).isNotNull().isEqualTo(1L);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/pets/{id} must return status 200 and PetResponse when shelter user find by id successfully")
  void getApiPetsId_MustReturnStatus200AndPetResponse_WhenShelterUserFindByIdSuccessfully() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    PetResponse actualResponseBody = webTestClient.get()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PetResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualResponseBody.description()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualResponseBody.age()).isNotNull().isEqualTo("2 years old");
    Assertions.assertThat(actualResponseBody.adopted()).isFalse();
    Assertions.assertThat(actualResponseBody.images()).isNotNull().hasSize(1);
    Assertions.assertThat(actualResponseBody.shelterId()).isNotNull().isEqualTo(1L);
  }


  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/pets/{id} must return status 400 and ProblemDetail when id is invalid")
  void getApiPetsId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.get()
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
  @Sql(scripts = {INSERT_SHELTER_PET_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/pets/{id} must return status 400 and ProblemDetail when pet not found")
  void getApiPetsId_MustReturnStatus400AndProblemDetail_WhenGuardianNotFound() {
    // Realizar Login
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    ProblemDetail actualResponseBody = webTestClient.get()
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
  @DisplayName("GET /api/pets must return status 401 when client do not send JWT")
  void getApiPets_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.get()
        .uri(generateUri("1"))
        .exchange()
        .expectStatus().isUnauthorized();
  }

  private String generateUri(String resourceId) {
    return String.format(GUARDIAN_URI_TEMPLATE, resourceId);
  }

}
