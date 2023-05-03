package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.IllegalOperationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class IllegalOperationExceptionHandler {

  @ExceptionHandler(IllegalOperationException.class)
  public ResponseEntity<ProblemDetail> handleIllegalOperationException(IllegalOperationException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/illegal-operation"));
    problemDetail.setTitle("Illegal operation");

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
