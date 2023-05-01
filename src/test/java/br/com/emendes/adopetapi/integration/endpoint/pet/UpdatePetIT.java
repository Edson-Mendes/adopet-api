package br.com.emendes.adopetapi.integration.endpoint.pet;

import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
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
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for PUT /api/pets/{id}")
class UpdatePetIT {

  @Autowired
  private WebTestClient webTestClient;

  private SignIn signIn;

  private static final String PET_URI_TEMPLATE = "/api/pets/%s";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/pets/{id} must return status 200 and PetResponse when update successfully")
  void putApiPetsId_MustReturnStatus200AndPetResponse_WhenUpdateSuccessfully() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdatePetRequest requestBody = UpdatePetRequest.builder()
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .image("http://www.imagesxpto.com/cats/1231")
        .build();

    PetResponse actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdatePetRequest.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody(PetResponse.class)
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("Darkness");
    Assertions.assertThat(actualResponseBody.description()).isNotNull().isEqualTo("A very cute cat");
    Assertions.assertThat(actualResponseBody.age()).isNotNull().isEqualTo("3 years old");
    Assertions.assertThat(actualResponseBody.adopted()).isFalse();
    Assertions.assertThat(actualResponseBody.images()).isNotNull().hasSize(1);
    Assertions.assertThat(actualResponseBody.shelterId()).isNotNull().isEqualTo(1L);
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/pets/{id} must return status 400 and ProblemDetail when id is invalid")
  void putApiPetsId_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdatePetRequest requestBody = UpdatePetRequest.builder()
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1o0"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdatePetRequest.class)
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
  @Sql(scripts = {INSERT_PET_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/pets/{id} must return status 400 and ProblemDetail when request body has invalid fields")
  void putApiPetsId_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdatePetRequest requestBody = UpdatePetRequest.builder()
        .name("")
        .description("A very cute cat")
        .age("3 years old")
        .image("wwwimagesxptocomcats1231476517632")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdatePetRequest.class)
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

    Assertions.assertThat(actualFields).isNotNull().contains("name", "image");
    Assertions.assertThat(actualMessages).isNotNull().contains("name must not be blank", "image must be a well-formed url");
  }

  @Test
  @Sql(scripts = {INSERT_MANY_SHELTERS_AND_PETS_SQL_PATH})
  @DisplayName("PUT /api/pets/{id} must return status 404 and ProblemDetail when a shelter tries to update a pet that does not belong to him")
  void putApiPetsId_MustReturnStatus404AndProblemDetail_WhenAShelterTriesToUpdateAPetThatDoesNotBelongToHim() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdatePetRequest requestBody = UpdatePetRequest.builder()
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdatePetRequest.class)
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
  @Sql(scripts = {INSERT_PET_SHELTER_SQL_PATH})
  @DisplayName("PUT /api/pets/{id} must return status 404 and ProblemDetail when pet not found")
  void putApiPetsId_MustReturnStatus404AndProblemDetail_WhenPetNotFound() {
    // Realizar Login antes da requisição.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());
    UpdatePetRequest requestBody = UpdatePetRequest.builder()
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    ProblemDetail actualResponseBody = webTestClient.put()
        .uri(generateUri("100"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdatePetRequest.class)
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
  @DisplayName("PUT /api/pets/{id} must return status 401 when client do not send JWT")
  void putApiPetsId_MustReturnStatus401_WhenClientDoNotSendJWT() {
    UpdatePetRequest requestBody = UpdatePetRequest.builder()
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    webTestClient.put()
        .uri(generateUri("1"))
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdatePetRequest.class)
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @Sql(scripts = {INSERT_PET_SHELTER_GUARDIAN_SQL_PATH})
  @DisplayName("PUT /api/pets/{id} must return status 403 when user is not a SHELTER")
  void putApiPetsId_MustReturnStatus403_WhenUserIsNotASHELTER() {
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    UpdatePetRequest requestBody = UpdatePetRequest.builder()
        .name("Darkness")
        .description("A very cute cat")
        .age("3 years old")
        .image("http://www.imagesxpto.com/cats/1231476517632")
        .build();

    webTestClient.put()
        .uri(generateUri("1"))
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), UpdatePetRequest.class)
        .exchange()
        .expectStatus().isForbidden();
  }

  private String generateUri(String resourceId) {
    return String.format(PET_URI_TEMPLATE, resourceId);
  }

}
