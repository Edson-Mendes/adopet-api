package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class PasswordsDoNotMatchExceptionHandler {

  @ExceptionHandler(PasswordsDoNotMatchException.class)
  public ResponseEntity<ProblemDetail> handlePasswordsDoNotMatch(PasswordsDoNotMatchException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/passwords-do-not-match"));
    problemDetail.setTitle("Passwords do not match");

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
