package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.service.AnimalShelterService;
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
@RequestMapping("/api/shelters")
public class ShelterController {

  private final AnimalShelterService animalShelterService;

  @PostMapping
  public ResponseEntity<AnimalShelterResponse> create(
      @RequestBody @Valid AnimalShelterRequest animalShelterRequest, UriComponentsBuilder uriComponentsBuilder) {
    AnimalShelterResponse animalShelterResponse = animalShelterService.create(animalShelterRequest);

    URI uri = uriComponentsBuilder.path("/api/shelters/{id}").build(animalShelterResponse.getId());

    return ResponseEntity.created(uri).body(animalShelterResponse);
  }

}
