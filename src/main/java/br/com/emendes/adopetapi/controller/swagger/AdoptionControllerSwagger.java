package br.com.emendes.adopetapi.controller.swagger;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@OpenAPIDefinition(tags = @Tag(name = "Adoption"), security = {@SecurityRequirement(name = "bearer-key")})
public interface AdoptionControllerSwagger {

  @Operation(summary = "Request an adoption", description = "xalala", tags = {"Adoption"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful adoption request",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdoptionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
          @ApiResponse(responseCode = "401", description = "Unauthorized, client did not send authorization header or sent an invalid one", content = @Content),
          @ApiResponse(responseCode = "403", description = "Forbidden, User does not have access permission", content = @Content),
          })
      ResponseEntity < AdoptionResponse > adopt(AdoptionRequest adoptionRequest, UriComponentsBuilder uriComponentsBuilder);
}
