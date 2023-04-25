package br.com.emendes.adopetapi.integration.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@DisplayName("Integration tests for POST /api/auth")
public class AuthenticationIT {

  private WebTestClient webTestClient = WebTestClient
      .bindToServer()
      .baseUrl("http://localhost:8080")
      .build();

}
