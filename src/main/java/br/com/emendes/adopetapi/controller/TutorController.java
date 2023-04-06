package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.service.TutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tutors")
public class TutorController {

  private final TutorService tutorService;

  @PostMapping
  public ResponseEntity<TutorResponse> create(
      @RequestBody @Valid CreateTutorRequest createTutorRequest, UriComponentsBuilder uriComponentsBuilder) {
    TutorResponse tutorResponse = tutorService.create(createTutorRequest);

    URI uri = uriComponentsBuilder.path("/api/tutor/{id}").build(tutorResponse.getId());

    return ResponseEntity.created(uri).body(tutorResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TutorResponse> update(
      @PathVariable(name = "id") Long id, @RequestBody @Valid UpdateTutorRequest updateTutorRequest) {
    return ResponseEntity.ok(tutorService.update(id, updateTutorRequest));
  }

}
