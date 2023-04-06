package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class EmailAlreadyInUseExceptionHandler {

  @ExceptionHandler(EmailAlreadyInUseException.class)
  public ResponseEntity<ProblemDetail> handleEmailAlreadyInUse(EmailAlreadyInUseException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/email-alreay-in-use"));
    problemDetail.setTitle("Email already in use");

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
