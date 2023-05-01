package br.com.emendes.adopetapi.controller.swagger;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
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

@Tag(name = "Shelter", description = "Shelter management APIs")
public interface ShelterControllerSwagger {

  @Operation(
      summary = "Create shelter user",
      description = "Create a shelter user. The response, if successful, is a JSON with information about created user.",
      tags = {"Shelter"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful create shelter user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShelterResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  ResponseEntity<ShelterResponse> create(CreateShelterRequest createShelterRequest, UriComponentsBuilder uriComponentsBuilder);

  @Operation(
      summary = "Update shelter",
      description = "Update shelter information by specifying its id on path and new data on request body. " +
          "A shelter can only update themself.",
      tags = {"Shelter"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Successful update",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShelterResponse.class))),
      @ApiResponse(
          responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content),
      @ApiResponse(
          responseCode = "403",
          description = "Forbidden, User does not have access permission", content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Shelter not found for current user",
          content = @Content)
  })
  ResponseEntity<ShelterResponse> update(Long id, UpdateShelterRequest updateShelterRequest);

  @Operation(
      summary = "Retrieve details about a shelter by id",
      description = "Get shelter by specifying its id. The response is a JSON with id, name and email.",
      tags = {"Shelter"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieve",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShelterResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Shelter not found",
          content = @Content)
  })
  ResponseEntity<ShelterResponse> findById(Long id);

  @Operation(
      summary = "Fetch Page of shelter",
      description = "Fetch paged shelter.",
      tags = {"Shelter"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful fetch",
          content = @Content),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content)
  })
  ResponseEntity<Page<ShelterResponse>> fetchAll(@ParameterObject Pageable pageable);

  @Operation(
      summary = "Delete shelter by id",
      description = "Delete shelter by specifying its id. A shelter user can only delete themself.",
      tags = {"Shelter"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
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
          description = "Shelter not found for current user",
          content = @Content)
  })
  ResponseEntity<Void> delete(Long id);

}
