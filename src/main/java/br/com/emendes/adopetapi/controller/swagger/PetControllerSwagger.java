package br.com.emendes.adopetapi.controller.swagger;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
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

@Tag(name = "Pet", description = "Pet management APIs")
@SecurityRequirement(name = SECURITY_SCHEME_KEY)
public interface PetControllerSwagger {

  @Operation(
      summary = "Register a pet",
      description = "Register a pet by sending name, description, age and image url in JSON file in the request body. " +
          "Only shelter user can register a pet.",
      tags = {"Pet"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful register pet",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(responseCode = "401", description = "Unauthorized, client did not send authorization header or sent an invalid one", content = @Content),
      @ApiResponse(responseCode = "403", description = "Forbidden, User does not have access permission", content = @Content),
  })
  ResponseEntity<PetResponse> create(CreatePetRequest createPetRequest, UriComponentsBuilder uriComponentsBuilder);

  @Operation(
      summary = "Fetch Page of pets",
      description = "Fetch page of non adopt pets.",
      tags = {"Pet"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful fetch",
          content = @Content),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content)
  })
  ResponseEntity<Page<PetResponse>> fetchAll(@ParameterObject Pageable pageable);

  @Operation(
      summary = "Retrieve details about a pet by id",
      description = "Get pet by specifying its id. The response is a JSON with id, name, description, age, adopted, images and shelterId .",
      tags = {"Pet"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieve",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Pet not found",
          content = @Content)
  })
  ResponseEntity<PetResponse> findById(Long id);

  @Operation(
      summary = "Update information about a pet",
      description = "Update pet by specifying its id on path and new date on request body. " +
          "Only the shelter that registered the pet can update it.",
      tags = {"Pet"})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Successful update",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PetResponse.class))),
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
          description = "Pet not found for current shelter user",
          content = @Content)
  })
  ResponseEntity<PetResponse> update(Long id, UpdatePetRequest updatePetRequest);

  @Operation(
      summary = "Delete pet by id",
      description = "Delete pet by specifying its id. Only the shelter that registered the pet can delete it. " +
          "Only pets non related to adoption can be deleted.",
      tags = {"Pet"})
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
          description = "Pet not found for current user",
          content = @Content)
  })
  ResponseEntity<Void> delete(Long id);

}
