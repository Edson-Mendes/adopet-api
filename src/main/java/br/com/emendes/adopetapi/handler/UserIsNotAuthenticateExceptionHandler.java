package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.UserIsNotAuthenticateException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class UserIsNotAuthenticateExceptionHandler {

  @ExceptionHandler(UserIsNotAuthenticateException.class)
  public ResponseEntity<ProblemDetail> handleUserIsNotAuthenticateException(UserIsNotAuthenticateException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/user-not-authenticate"));
    problemDetail.setTitle("Something went wrong");

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
