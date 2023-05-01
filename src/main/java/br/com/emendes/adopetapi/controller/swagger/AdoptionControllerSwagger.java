package br.com.emendes.adopetapi.controller.swagger;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static br.com.emendes.adopetapi.config.OpenApiConfig.SECURITY_SCHEME_KEY;

@Tag(name = "Adoption", description = "Adoption management APIs")
@SecurityRequirement(name = SECURITY_SCHEME_KEY)
public interface AdoptionControllerSwagger {

  @Operation(
      summary = "Request an adoption",
      description = "Request an adoption by specifying a petId on request body. The response, if successful, is a JSON with information about requested adoption.",
      tags = {"Adoption"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful adoption request",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdoptionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized, client did not send authorization header or sent an invalid one", content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden, User does not have access permission", content = @Content),
  })
  ResponseEntity<AdoptionResponse> adopt(AdoptionRequest adoptionRequest, UriComponentsBuilder uriComponentsBuilder);

  @Operation(
      summary = "Fetch Page of adoption",
      description = "Fetch paged adoption related to current user",
      tags = {"Adoption"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful fetch",
          content = @Content),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content)
  })
  ResponseEntity<Page<AdoptionResponse>> fetchAll(@ParameterObject Pageable pageable);

  @Operation(
      summary = "Retrieve details about a adoption by id",
      description = "Get Adoption related to current user by specifying its id. The response is a JSON with id, petId, guardianId, status and date.",
      tags = {"Adoption"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieve",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdoptionResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Adoption not found for current user",
          content = @Content)
  })
  ResponseEntity<AdoptionResponse> findById(Long id);

  @Operation(
      summary = "Update status of an adoption",
      description = "Update status of an Adoption related to current user by specifying its id on path and new status on request body.",
      tags = {"Adoption"})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Successful update",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdoptionResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden, User does not have access permission", content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Adoption not found for current user",
          content = @Content)
  })
  ResponseEntity<AdoptionResponse> updateStatus(Long id, UpdateStatusRequest updateStatusRequest);

  @Operation(
      summary = "Delete adoption by id",
      description = "Delete Adoption related to current shelter user by specifying its id.",
      tags = {"Adoption"})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Successful delete",
          content = @Content),
      @ApiResponse(
          responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden, User does not have access permission", content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Adoption not found for current user",
          content = @Content)
  })
  ResponseEntity<Void> delete(Long id);

}
