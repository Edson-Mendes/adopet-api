package br.com.emendes.adopetapi.integration.pet;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
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
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_SQL_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for POST /api/pets")
class CreatePetIT {

  @Autowired
  private WebTestClient webTestClient;
  private SignIn signIn;

  private static final String PET_URI = "/api/pets";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("POST /api/pets must return status 201 and PetResponse when create successfully")
  void postApiPets_MustReturnStatus201AndPetResponse_WhenCreateSuccessfully() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    CreatePetRequest requestBody = CreatePetRequest.builder()
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    PetResponse actualResponseBody = webTestClient.post()
        .uri(PET_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), CreatePetRequest.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(PetResponse.class).returnResult().getResponseBody();

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
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("POST /api/pets must return status 400 and ProblemDetail when request body has invalid fields")
  void postApiPets_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    CreatePetRequest requestBody = CreatePetRequest.builder()
        .name("")
        .description("A very calm and cute cat")
        .age("2 years old")
        .image("imagesxpto.com/cats/1231476517632")
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(PET_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), CreatePetRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Invalid arguments");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("Some fields are invalid");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);

    Assertions.assertThat(actualResponseBody.getProperties()).isNotNull();

    String actualFields = (String) actualResponseBody.getProperties().get("fields");
    String actualMessages = (String) actualResponseBody.getProperties().get("messages");

    Assertions.assertThat(actualFields).isNotNull().contains("name", "image");
    Assertions.assertThat(actualMessages).isNotNull().contains("name must not be blank", "image must be a well-formed url");
  }

  @Test
  @DisplayName("POST /api/pets must return status 401 when client do not send JWT")
  void postApiPets_MustReturnStatus401_WhenClientDoNotSendJWT() {
    CreatePetRequest requestBody = CreatePetRequest.builder()
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    webTestClient.post()
        .uri(PET_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), CreatePetRequest.class)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("POST /api/pets must return status 403 when user not is a SHELTER")
  void postApiPets_MustReturnStatus403_WhenUserNotIsAShelter() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    CreatePetRequest requestBody = CreatePetRequest.builder()
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    webTestClient.post()
        .uri(PET_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .body(Mono.just(requestBody), CreatePetRequest.class)
        .exchange()
        .expectStatus().isForbidden();
  }

}
