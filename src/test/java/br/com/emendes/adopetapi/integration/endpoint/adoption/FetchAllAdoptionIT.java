package br.com.emendes.adopetapi.integration.endpoint.adoption;

import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.repository.AdoptionRepository;
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
import static br.com.emendes.adopetapi.util.sql.SqlPath.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration")
@DisplayName("Integration tests for GET /api/adoptions")
class FetchAllAdoptionIT {

  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private AdoptionRepository adoptionRepository;

  private SignIn signIn;

  private static final String ADOPTION_URI = "/api/adoptions";

  @BeforeEach
  void setUp() {
    signIn = new SignIn(webTestClient);
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTIONS_MANY_SHELTERS_SQL_PATH})
  @DisplayName("GET /api/adoptions must return status 200 and only adoptions related to the user when shelter user fetch all successfully")
  void getApiAdoptions_MustReturnStatus200AndOnlyAdoptionsRelatedToTheUser_WhenShelterUserFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(shelterAuthenticationRequest());

    Page<AdoptionResponse> actualResponseBody = webTestClient.get()
        .uri(ADOPTION_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<AdoptionResponse>>() {
        })
        .returnResult().getResponseBody();

    long actualAdoptionsAmount = adoptionRepository.count();

    Assertions.assertThat(actualAdoptionsAmount).isEqualTo(4L);

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    AdoptionResponse actualAdoptionResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualAdoptionResponse.id()).isNotNull();
    Assertions.assertThat(actualAdoptionResponse.pet()).isNotNull();
    Assertions.assertThat(actualAdoptionResponse.pet().id()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualAdoptionResponse.guardianId()).isNotNull().isEqualTo(1L);
  }

  @Test
  @Sql(scripts = {INSERT_ADOPTIONS_MANY_GUARDIANS_SQL_PATH})
  @DisplayName("GET /api/adoptions must return status 200 and only adoptions related to the user when guardian user fetch all successfully")
  void getApiAdoptions_MustReturnStatus200AndOnlyAdoptionsRelatedToTheUser_WhenGuardianUserFetchAllSuccessfully() {
    // Realizar Login.
    String authorizationHeaderValue = signIn.run(guardianAuthenticationRequest());

    Page<AdoptionResponse> actualResponseBody = webTestClient.get()
        .uri(ADOPTION_URI)
        .header(AUTHORIZATION_HEADER_NAME, authorizationHeaderValue)
        .exchange()
        .expectStatus().isOk()
        .expectBody(new ParameterizedTypeReference<PageableResponse<AdoptionResponse>>() {
        })
        .returnResult().getResponseBody();

    long actualAdoptionsAmount = adoptionRepository.count();

    Assertions.assertThat(actualAdoptionsAmount).isEqualTo(4L);

    Assertions.assertThat(actualResponseBody).isNotNull().hasSize(1);

    AdoptionResponse actualAdoptionResponse = actualResponseBody.stream().toList().get(0);

    Assertions.assertThat(actualAdoptionResponse.id()).isNotNull();
    Assertions.assertThat(actualAdoptionResponse.pet()).isNotNull();
    Assertions.assertThat(actualAdoptionResponse.pet().id()).isNotNull().isEqualTo(1L);
    Assertions.assertThat(actualAdoptionResponse.guardianId()).isNotNull().isEqualTo(1L);
  }

  @Test
  @DisplayName("GET /api/adoptions must return status 401 when client do not send JWT")
  void getApiAdoptions_MustReturnStatus401_WhenClientDoNotSendJWT() {
    webTestClient.get()
        .uri(ADOPTION_URI)
        .exchange()
        .expectStatus().isUnauthorized();
  }

}
