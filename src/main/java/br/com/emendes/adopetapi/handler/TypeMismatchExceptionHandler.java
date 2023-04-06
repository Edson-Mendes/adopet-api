package br.com.emendes.adopetapi.handler;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;

@RestControllerAdvice
public class TypeMismatchExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ProblemDetail> handleEmailAlreadyInUse(MethodArgumentTypeMismatchException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), "An error occurred trying to cast String to Number");

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/type-mismatch"));
    problemDetail.setTitle("Type mismatch");

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
