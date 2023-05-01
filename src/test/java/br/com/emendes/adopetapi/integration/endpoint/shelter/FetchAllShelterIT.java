package br.com.emendes.adopetapi.integration.endpoint.shelter;

import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.util.PageableResponse;
import br.com.emendes.adopetapi.util.component.SignIn;
import lombok.extern.slf4j.Slf4j;
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

import static br.com.emendes.adopetapi.util.AuthenticationUtils.guardianAuthenticationRequest;
import static br.com.emendes.adopetapi.util.AuthenticationUtils.shelterAuthenticationRequest;
import static br.com.emendes.adopetapi.util.ConstantUtils.AUTHORIZATION_HEADER_NAME;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_AND_SHELTER_SQL_PATH;
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_SHELTER_SQL_PATH;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for GET /api/shelters")
class FetchAllShelterIT {

  @Autowired
  private WebTestClient webTestClient;

  private SignIn signIn;

  private static final String SHELTER_URI = "/api/shelters";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_SHELTER_SQL_PATH})
  @DisplayName("GET /api/shelters must return status 200 and Page<ShelterResponse> when fetch all successfully")
  void getApiShelters_MustReturnStatus200AndPageShelterResponse_WhenFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    Page<ShelterResponse> actualResponseBody = webTestClient.get()
        .uri(SHELTER_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<ShelterResponse>>() {
        })
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    ShelterResponse actualShelterResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualShelterResponse.id()).isNotNull();
    Assertions.assertThat(actualShelterResponse.name()).isNotNull().isEqualTo("Animal Shelter");
    Assertions.assertThat(actualShelterResponse.email()).isNotNull().isEqualTo("animal.shelter@email.com");
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_AND_SHELTER_SQL_PATH})
  @DisplayName("GET /api/shelters must return status 200 and Page<GuardianResponse> when guardian user fetch all successfully")
  void getApiShelters_MustReturnStatus200AndPageGuardianResponse_WhenGuardianUserFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    Page<ShelterResponse> actualResponseBody = webTestClient.get()
        .uri(SHELTER_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<ShelterResponse>>() {
        })
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    ShelterResponse actualShelterResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualShelterResponse.id()).isNotNull();
    Assertions.assertThat(actualShelterResponse.name()).isNotNull().isEqualTo("Animal Shelter");
    Assertions.assertThat(actualShelterResponse.email()).isNotNull().isEqualTo("animal.shelter@email.com");
  }

  @Test
  @DisplayName("GET /api/shelters must return status 401 when client do not send JWT")
  void getApiShelters_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.get()
        .uri(SHELTER_URI)
        .exchange()
        .expectStatus().isUnauthorized();
  }

}
