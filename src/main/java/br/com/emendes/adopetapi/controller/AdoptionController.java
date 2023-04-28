package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.controller.swagger.AdoptionControllerSwagger;
import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.service.AdoptionService;
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
@RequestMapping("/api/adoptions")
public class AdoptionController implements AdoptionControllerSwagger {

  private final AdoptionService adoptionService;

  @PostMapping
  public ResponseEntity<AdoptionResponse> adopt(
      @RequestBody @Valid AdoptionRequest adoptionRequest, UriComponentsBuilder uriComponentsBuilder) {
    AdoptionResponse adoptionResponse = adoptionService.adopt(adoptionRequest);
    URI uri = uriComponentsBuilder.path("/api/adoptions/{id}").build(adoptionResponse.id());

    return ResponseEntity.created(uri).body(adoptionResponse);
  }

  @GetMapping
  public ResponseEntity<Page<AdoptionResponse>> fetchAll(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(adoptionService.fetchAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AdoptionResponse> findById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(adoptionService.findById(id));
  }

  @PutMapping("/{id}/status")
  public ResponseEntity<AdoptionResponse> updateStatus(
      @PathVariable(name = "id") Long id, @RequestBody @Valid UpdateStatusRequest updateStatusRequest) {
    return ResponseEntity.ok(adoptionService.updateStatus(id, updateStatusRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
    adoptionService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
