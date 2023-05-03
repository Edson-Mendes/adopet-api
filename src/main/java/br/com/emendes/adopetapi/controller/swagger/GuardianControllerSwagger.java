package br.com.emendes.adopetapi.controller.swagger;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
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

@Tag(name = "Guardian", description = "Guardian management APIs")
public interface GuardianControllerSwagger {

  @Operation(
      summary = "Create guardian user",
      description = "Create a guardian user. The response, if successful, is a JSON with information about created user.",
      tags = {"Guardian"}
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful create guardian user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuardianResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
  })
  ResponseEntity<GuardianResponse> create(CreateGuardianRequest createGuardianRequest, UriComponentsBuilder uriComponentsBuilder);

  @Operation(
      summary = "Update guardian",
      description = "Update guardian information by specifying its id on path and new data on request body. " +
          "A guardian can only update themself.",
      tags = {"Guardian"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Successful update",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuardianResponse.class))),
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
          description = "Guardian not found for current user",
          content = @Content)
  })
  ResponseEntity<GuardianResponse> update(Long id, UpdateGuardianRequest updateGuardianRequest);

  @Operation(
      summary = "Retrieve details about a guardian by id",
      description = "Get guardian by specifying its id. The response is a JSON with id, name and email.",
      tags = {"Guardian"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful retrieve",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuardianResponse.class))),
      @ApiResponse(responseCode = "400", description = "Something is wrong with the request",
          content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content),
      @ApiResponse(
          responseCode = "404",
          description = "Guardian not found",
          content = @Content)
  })
  ResponseEntity<GuardianResponse> findById(Long id);

  @Operation(
      summary = "Fetch Page of guardian",
      description = "Fetch paged guardian.",
      tags = {"Guardian"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful fetch",
          content = @Content),
      @ApiResponse(
          responseCode = "401",
          description = "Unauthorized, client did not send authorization header or sent an invalid one",
          content = @Content)
  })
  ResponseEntity<Page<GuardianResponse>> fetchAll(@ParameterObject Pageable pageable);

  @Operation(
      summary = "Delete guardian by id",
      description = "Delete guardian by specifying its id. A guardian user can only delete themself.",
      tags = {"Guardian"},
      security = {@SecurityRequirement(name = SECURITY_SCHEME_KEY)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "Successful delete",
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
          description = "Guardian not found for current user",
          content = @Content)
  })
  ResponseEntity<Void> delete(Long id);

}
