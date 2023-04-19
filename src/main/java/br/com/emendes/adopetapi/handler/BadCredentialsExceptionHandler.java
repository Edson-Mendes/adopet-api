package br.com.emendes.adopetapi.handler;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class BadCredentialsExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/bad-credentials"));
    problemDetail.setTitle("Bad Credentials");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ProblemDetail> handleDisabledException(DisabledException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/bad-credentials"));
    problemDetail.setTitle("Bad Credentials");

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
