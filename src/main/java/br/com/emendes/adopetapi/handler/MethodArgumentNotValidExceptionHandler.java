package br.com.emendes.adopetapi.handler;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    String fields = exception.getFieldErrors().stream().map(FieldError::getField)
        .collect(Collectors.joining("; "));
    String messages = exception.getFieldErrors().stream().map(FieldError::getDefaultMessage)
        .collect(Collectors.joining("; "));

    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), "Some fields are invalid");

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/invalid-fields"));
    problemDetail.setTitle("Invalid arguments");
    problemDetail.setProperty("fields", fields);
    problemDetail.setProperty("messages", messages);

    return ResponseEntity.badRequest().body(problemDetail);
  }

}
