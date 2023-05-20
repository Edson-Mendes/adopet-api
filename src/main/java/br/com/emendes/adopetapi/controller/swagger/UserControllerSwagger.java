package br.com.emendes.adopetapi.controller.swagger;

import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static br.com.emendes.adopetapi.config.OpenApiConfig.SECURITY_SCHEME_KEY;

@Tag(name = "User", description = "User management APIs")
@SecurityRequirement(name = SECURITY_SCHEME_KEY)
public interface UserControllerSwagger {

  @Operation(
      summary = "Update user password",
      description = "Endpoint to user update your password. " +
          "The user must be authenticated.",
      tags = {"User"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful update password",
          content = @Content),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one", content = @Content),
  })
  ResponseEntity<Void> updatePassword(UpdatePasswordRequest updatePasswordRequest);

}
