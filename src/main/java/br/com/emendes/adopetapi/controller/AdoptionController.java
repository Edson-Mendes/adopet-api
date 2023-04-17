package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.service.AdoptionService;
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
@RequestMapping("/api/adoptions")
public class AdoptionController {

  private final AdoptionService adoptionService;

  @PostMapping
  public ResponseEntity<AdoptionResponse> adopt(
      @RequestBody @Valid AdoptionRequest adoptionRequest, UriComponentsBuilder uriComponentsBuilder) {
    AdoptionResponse adoptionResponse = adoptionService.adopt(adoptionRequest);
    URI uri = uriComponentsBuilder.path("/api/adoptions/{id}").build(adoptionResponse.getId());

    return ResponseEntity.created(uri).body(adoptionResponse);
  }

}
