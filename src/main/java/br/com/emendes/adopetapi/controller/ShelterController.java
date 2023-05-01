package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.controller.swagger.ShelterControllerSwagger;
import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.service.ShelterService;
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
@RequestMapping("/api/shelters")
public class ShelterController implements ShelterControllerSwagger {

  private final ShelterService shelterService;

  @PostMapping
  public ResponseEntity<ShelterResponse> create(
      @RequestBody @Valid CreateShelterRequest createShelterRequest, UriComponentsBuilder uriComponentsBuilder) {
    ShelterResponse shelterResponse = shelterService.create(createShelterRequest);

    URI uri = uriComponentsBuilder.path("/api/shelters/{id}").build(shelterResponse.id());

    return ResponseEntity.created(uri).body(shelterResponse);
  }

  @GetMapping
  public ResponseEntity<Page<ShelterResponse>> fetchAll(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(shelterService.fetchAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ShelterResponse> findById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(shelterService.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ShelterResponse> update(
      @PathVariable(name = "id") Long id, @RequestBody @Valid UpdateShelterRequest updateShelterRequest) {
    return ResponseEntity.ok(shelterService.update(id, updateShelterRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
    shelterService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
