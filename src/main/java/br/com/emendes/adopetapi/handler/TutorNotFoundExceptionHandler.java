package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.TutorNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class TutorNotFoundExceptionHandler {

  @ExceptionHandler(TutorNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleTutorNotFound(TutorNotFoundException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/entity-not-found"));
    problemDetail.setTitle("Tutor not found");

    return ResponseEntity.status(404).body(problemDetail);
  }

}
