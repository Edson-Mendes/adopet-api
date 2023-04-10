package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
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
public class ShelterController {

  private final ShelterService shelterService;

  @PostMapping
  public ResponseEntity<ShelterResponse> create(
      @RequestBody @Valid ShelterRequest shelterRequest, UriComponentsBuilder uriComponentsBuilder) {
    ShelterResponse shelterResponse = shelterService.create(shelterRequest);

    URI uri = uriComponentsBuilder.path("/api/shelters/{id}").build(shelterResponse.getId());

    return ResponseEntity.created(uri).body(shelterResponse);
  }

  @GetMapping
  public ResponseEntity<Page<ShelterResponse>> fetchAll(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(shelterService.fetchAll(pageable));
  }

}
