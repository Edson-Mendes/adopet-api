package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class ShelterNotFoundExceptionHandler {

  @ExceptionHandler(ShelterNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleShelterNotFound(ShelterNotFoundException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/entity-not-found"));
    problemDetail.setTitle("Shelter not found");

    return ResponseEntity.status(404).body(problemDetail);
  }

}
