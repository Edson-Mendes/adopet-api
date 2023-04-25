package br.com.emendes.adopetapi.util.component;

import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;

@Profile("integration")
public class SignIn {

  private final WebTestClient webTestClient;

  public SignIn(WebTestClient webTestClient) {
    if (webTestClient == null)
      throw new IllegalArgumentException("webTestClient must not be null");
    this.webTestClient = webTestClient;
  }

  /**
   * @param request com os dados do usuário.
   * @return O header Authorization "Bearer {token}"
   */
  public String run(AuthenticationRequest request) {
    String token = webTestClient.post()
        .uri("/api/auth")
        .header("Content-Type", CONTENT_TYPE)
        .body(Mono.just(request), CreateShelterRequest.class)
        .exchange()
        .expectBody(AuthenticationResponse.class).returnResult().getResponseBody().token();

    return String.format("Bearer %s", token);
  }

}
