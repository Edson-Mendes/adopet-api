package br.com.emendes.adopetapi.integration.endpoint.shelter;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
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

import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE_HEADER_NAME;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_SQL_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for POST /api/shelters")
class CreateShelterIT {

  @Autowired
  private WebTestClient webTestClient;

  private static final String SHELTER_URI = "/api/shelters";

  @Test
  @DisplayName("POST /api/shelters must return status 201 and ShelterResponse when create successfully")
  void postApiShelters_MustReturnStatus201AndShelterResponse_WhenCreateSuccessfully() {
    CreateShelterRequest requestBody = CreateShelterRequest.builder()
        .name("Animal Shelter")
        .email("animal.shelter@email.com")
        .password("1234567890")
        .confirmPassword("1234567890")
        .build();

    ShelterResponse actualResponseBody = webTestClient.post()
        .uri(SHELTER_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), CreateShelterRequest.class)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(ShelterResponse.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.id()).isNotNull();
    Assertions.assertThat(actualResponseBody.name()).isNotNull().isEqualTo("Animal Shelter");
    Assertions.assertThat(actualResponseBody.email()).isNotNull().isEqualTo("animal.shelter@email.com");
  }

  @Test
  @DisplayName("POST /api/shelters must return status 400 and ProblemDetail when passwords do not match")
  void postApiShelters_MustReturnStatus400AndProblemDetail_WhenPasswordsDoNotMatch() {
    CreateShelterRequest requestBody = CreateShelterRequest.builder()
        .name("Animal Shelter")
        .email("animal.shelter@email.com")
        .password("1234567890")
        .confirmPassword("12345678")
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(SHELTER_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), CreateShelterRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Passwords do not match");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull().isEqualTo("Passwords do not match");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/shelters");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("POST /api/shelters must return status 400 and ProblemDetail when email already exists")
  void postApiShelters_MustReturnStatus400AndProblemDetail_WhenEmailAlreadyExists() {
    CreateShelterRequest requestBody = CreateShelterRequest.builder()
        .name("Animal Shelter")
        .email("animal.shelter@email.com")
        .password("1234567890")
        .confirmPassword("1234567890")
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(SHELTER_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), CreateShelterRequest.class)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ProblemDetail.class).returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull();
    Assertions.assertThat(actualResponseBody.getTitle()).isNotNull().isEqualTo("Email already in use");
    Assertions.assertThat(actualResponseBody.getDetail()).isNotNull()
        .isEqualTo("E-mail {animal.shelter@email.com} is already in use");
    Assertions.assertThat(actualResponseBody.getInstance()).isNotNull();
    Assertions.assertThat(actualResponseBody.getInstance().getPath()).isNotNull().isEqualTo("/api/shelters");
    Assertions.assertThat(actualResponseBody.getStatus()).isEqualTo(400);
  }

  @Test
  @DisplayName("POST /api/shelters must return status 400 and ProblemDetail when request body has invalid fields")
  void postApiShelters_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() {
    CreateShelterRequest requestBody = CreateShelterRequest.builder()
        .name("")
        .email("animalshelteremail.com")
        .password("1234567890")
        .confirmPassword("1234567890")
        .build();

    ProblemDetail actualResponseBody = webTestClient.post()
        .uri(SHELTER_URI)
        .header(CONTENT_TYPE_HEADER_NAME, CONTENT_TYPE)
        .body(Mono.just(requestBody), CreateShelterRequest.class)
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

    Assertions.assertThat(actualFields).isNotNull().contains("name", "email");
    Assertions.assertThat(actualMessages).isNotNull().contains("name must not be blank", "must be a well formed email");
  }

}
