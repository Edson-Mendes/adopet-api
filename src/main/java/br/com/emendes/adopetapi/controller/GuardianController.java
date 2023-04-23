package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.service.GuardianService;
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
@RequestMapping("/api/guardians")
public class GuardianController {

  private final GuardianService guardianService;

  @PostMapping
  public ResponseEntity<GuardianResponse> create(
      @RequestBody @Valid CreateGuardianRequest createGuardianRequest, UriComponentsBuilder uriComponentsBuilder) {
    GuardianResponse guardianResponse = guardianService.create(createGuardianRequest);

    URI uri = uriComponentsBuilder.path("/api/guardians/{id}").build(guardianResponse.id());

    return ResponseEntity.created(uri).body(guardianResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<GuardianResponse> update(
      @PathVariable(name = "id") Long id, @RequestBody @Valid UpdateGuardianRequest updateGuardianRequest) {
    return ResponseEntity.ok(guardianService.update(id, updateGuardianRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<GuardianResponse> findById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(guardianService.findById(id));
  }

  @GetMapping
  public ResponseEntity<Page<GuardianResponse>> fetchAll(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(guardianService.fetchAll(pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
    guardianService.deleteById(id);

    return ResponseEntity.noContent().build();
  }

}
