package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.service.TutorService;
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
@RequestMapping("/api/tutors")
public class TutorController {

  private final TutorService tutorService;

  @PostMapping
  public ResponseEntity<TutorResponse> create(
      @RequestBody TutorRequest tutorRequest, UriComponentsBuilder uriComponentsBuilder) {
    TutorResponse tutorResponse = tutorService.create(tutorRequest);

    URI uri = uriComponentsBuilder.path("/api/tutor/{id}").build(tutorResponse.getId());

    return ResponseEntity.created(uri).body(tutorResponse);
  }

}
