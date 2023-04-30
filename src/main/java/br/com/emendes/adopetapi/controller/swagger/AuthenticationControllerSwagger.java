package br.com.emendes.adopetapi.controller.swagger;

import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import br.com.emendes.adopetapi.dto.response.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Authentication management APIs")
public interface AuthenticationControllerSwagger {

  @Operation(
      summary = "Perform authentication",
      description = "Endpoint to user perform authentication. The response, if successful, is a JSON with JWT.",
      tags = {"Authentication"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful user authentication",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest authRequest);

}
