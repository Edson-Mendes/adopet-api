package br.com.emendes.adopetapi.handler;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class PropertyReferenceExceptionHandler {

  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<ProblemDetail> handleGuardianNotFound(PropertyReferenceException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/no-property-found"));
    problemDetail.setTitle("Property not found");

    return ResponseEntity.status(400).body(problemDetail);
  }

}
