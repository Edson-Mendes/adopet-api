package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @GetMapping
  public ResponseEntity<Page<PetResponse>> fetchAll(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(petService.fetchAll(pageable));
  }

  @GetMapping("/{id}")
  public  ResponseEntity<PetResponse> findById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(petService.findById(id));
  }

  @PutMapping("/{id}")
  public  ResponseEntity<PetResponse> update(
      @PathVariable(name = "id") Long id, @RequestBody @Valid UpdatePetRequest updatePetRequest) {
    return ResponseEntity.ok(petService.update(id, updatePetRequest));
  }

}
