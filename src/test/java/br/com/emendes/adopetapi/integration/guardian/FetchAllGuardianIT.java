package br.com.emendes.adopetapi.integration.guardian;

import br.com.emendes.adopetapi.dto.response.GuardianResponse;
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
import static br.com.emendes.adopetapi.util.sql.SqlPath.INSERT_GUARDIAN_SQL_PATH;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for GET /api/guardians")
class FetchAllGuardianIT {

  @Autowired
  private WebTestClient webTestClient;

  private SignIn signIn;

  private static final String GUARDIAN_URI = "/api/guardians";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_SQL_PATH})
  @DisplayName("GET /api/guardians must return status 200 and Page<GuardianResponse> when fetch all successfully")
  void getApiGuardians_MustReturnStatus200AndPageGuardianResponse_WhenFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    Page<GuardianResponse> actualResponseBody = webTestClient.get()
        .uri(GUARDIAN_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<GuardianResponse>>() {
        })
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    GuardianResponse actualGuardianResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualGuardianResponse.id()).isNotNull();
    Assertions.assertThat(actualGuardianResponse.name()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualGuardianResponse.email()).isNotNull().isEqualTo("lorem@email.com");
  }

  @Test
  @Sql(scripts = {INSERT_GUARDIAN_AND_SHELTER_SQL_PATH})
  @DisplayName("GET /api/guardians must return status 200 and Page<GuardianResponse> when shelter user fetch all successfully")
  void getApiGuardians_MustReturnStatus200AndPageGuardianResponse_WhenShelterUserFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    Page<GuardianResponse> actualResponseBody = webTestClient.get()
        .uri(GUARDIAN_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<GuardianResponse>>() {
        })
        .returnResult().getResponseBody();

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    GuardianResponse actualGuardianResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualGuardianResponse.id()).isNotNull();
    Assertions.assertThat(actualGuardianResponse.name()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualGuardianResponse.email()).isNotNull().isEqualTo("lorem@email.com");
  }

  @Test
  @DisplayName("GET /api/guardians must return status 401 when client do not send JWT")
  void getApiGuardians_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.get()
        .uri(GUARDIAN_URI)
        .exchange()
        .expectStatus().isUnauthorized();
  }

}
