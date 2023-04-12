package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.PetNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class PetNotFoundExceptionHandler {

  @ExceptionHandler(PetNotFoundException.class)
  public ResponseEntity<ProblemDetail> handlePetNotFound(PetNotFoundException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/entity-not-found"));
    problemDetail.setTitle("Pet not found");

    return ResponseEntity.status(404).body(problemDetail);
  }

}
