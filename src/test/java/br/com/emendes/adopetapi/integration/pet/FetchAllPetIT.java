package br.com.emendes.adopetapi.integration.pet;

import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.util.PageableResponse;
import br.com.emendes.adopetapi.util.component.SignIn;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.AUTHORIZATION_HEADER_NAME;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_PET_GUARDIAN_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_PET_SQL_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for GET /api/pets")
class FetchAllPetIT {

  @Autowired
  private WebTestClient webTestClient;

  private SignIn signIn;

  private static final String PET_URI = "/api/pets";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_SQL_PATH})
  @DisplayName("GET /api/pets must return status 200 and Page<PetResponse> when guardian user fetch all successfully")
  void getApiPets_MustReturnStatus200AndPagePetResponse_WhenGuardianUserFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    Page<PetResponse> actualResponseBody = webTestClient.get()
        .uri(PET_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<PetResponse>>() {
        })
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    PetResponse actualPetResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualPetResponse.id()).isNotNull();
    Assertions.assertThat(actualPetResponse.name()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualPetResponse.description()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualPetResponse.age()).isNotNull().isEqualTo("2 years old");
    Assertions.assertThat(actualPetResponse.adopted()).isFalse();
    Assertions.assertThat(actualPetResponse.images()).isNotNull().hasSize(1);
    Assertions.assertThat(actualPetResponse.shelterId()).isNotNull().isEqualTo(1L);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_PET_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/pets must return status 200 and Page<PetResponse> when shelter user fetch all successfully")
  void getApiPets_MustReturnStatus200AndPagePetResponse_WhenShelterUserFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    Page<PetResponse> actualResponseBody = webTestClient.get()
        .uri(PET_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<PetResponse>>() {
        })
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    PetResponse actualPetResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualPetResponse.id()).isNotNull();
    Assertions.assertThat(actualPetResponse.name()).isNotNull().isEqualTo("Dark");
    Assertions.assertThat(actualPetResponse.description()).isNotNull().isEqualTo("A very calm and cute cat");
    Assertions.assertThat(actualPetResponse.age()).isNotNull().isEqualTo("2 years old");
    Assertions.assertThat(actualPetResponse.adopted()).isFalse();
    Assertions.assertThat(actualPetResponse.images()).isNotNull().hasSize(1);
    Assertions.assertThat(actualPetResponse.shelterId()).isNotNull().isEqualTo(1L);
  }

  @Test
  @DisplayName("GET /api/pets must return status 401 when client do not send JWT")
  void getApiPets_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.get()
        .uri(PET_URI)
        .exchange()
        .expectStatus().isUnauthorized();
  }

}
