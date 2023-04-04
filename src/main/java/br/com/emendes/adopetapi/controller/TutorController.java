package br.com.emendes.adopetapi.controller;

import br.com.emendes.adopetapi.dto.request.TutorRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tutor")
public class TutorController {

  @PostMapping
  public void create(@RequestBody TutorRequest tutorRequest) {

  }

}
