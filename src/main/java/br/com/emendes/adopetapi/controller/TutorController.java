package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.service.TutorService;
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

  @GetMapping("/{id}")
  public ResponseEntity<TutorResponse> findById(@PathVariable(name = "id") Long id) {
    return ResponseEntity.ok(tutorService.findById(id));
  }

  @GetMapping
  public ResponseEntity<Page<TutorResponse>> fetchAll(@PageableDefault Pageable pageable) {
    return ResponseEntity.ok(tutorService.fetchAll(pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
    tutorService.deleteById(id);

    return ResponseEntity.noContent().build();
  }

}
