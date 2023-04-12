package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pets")
public class PetController {

  private final PetService petService;

  @PostMapping
  public ResponseEntity<PetResponse> create(
      @RequestBody @Valid CreatePetRequest createPetRequest, UriComponentsBuilder uriComponentsBuilder) {
    PetResponse petResponse = petService.create(createPetRequest);

    URI uri = uriComponentsBuilder.path("/api/pets/{id}").build(petResponse.getId());

    return ResponseEntity.created(uri).body(petResponse);
  }

}
