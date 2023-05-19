package br.com.emendes.adopetapi.handler;

import br.com.emendes.adopetapi.exception.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String ENTITY_NOT_FOUND_INFO_URL = "https://github.com/Edson-Mendes/adopet-api/problem-detail/entity-not-found";

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
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

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException exception,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/message-not-readable"));
    problemDetail.setTitle("Message not readable");

    return ResponseEntity.status(400).body(problemDetail);
  }

  @ExceptionHandler(InvalidArgumentException.class)
  public ResponseEntity<ProblemDetail> handleInvalidArgumentException(InvalidArgumentException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/invalid-argument"));
    problemDetail.setTitle("Something went wrong");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(IllegalOperationException.class)
  public ResponseEntity<ProblemDetail> handleIllegalOperationException(IllegalOperationException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/illegal-operation"));
    problemDetail.setTitle("Illegal operation");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), "Incorrect email or password");

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/bad-credentials"));
    problemDetail.setTitle(exception.getMessage());

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

  @ExceptionHandler(UserIsNotAuthenticateException.class)
  public ResponseEntity<ProblemDetail> handleUserIsNotAuthenticateException(UserIsNotAuthenticateException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/user-not-authenticate"));
    problemDetail.setTitle("Something went wrong");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(EmailAlreadyInUseException.class)
  public ResponseEntity<ProblemDetail> handleEmailAlreadyInUse(EmailAlreadyInUseException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/email-alreay-in-use"));
    problemDetail.setTitle("Email already in use");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), "An error occurred trying to cast String to Number");

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/type-mismatch"));
    problemDetail.setTitle("Type mismatch");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<ProblemDetail> handleGuardianNotFound(PropertyReferenceException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/no-property-found"));
    problemDetail.setTitle("Property not found");

    return ResponseEntity.status(400).body(problemDetail);
  }

  @ExceptionHandler(PasswordsDoNotMatchException.class)
  public ResponseEntity<ProblemDetail> handlePasswordsDoNotMatch(PasswordsDoNotMatchException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/passwords-do-not-match"));
    problemDetail.setTitle("Passwords do not match");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(WrongPasswordException.class)
  public ResponseEntity<ProblemDetail> handleWrongPassword(WrongPasswordException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(400), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/wrong-password"));
    problemDetail.setTitle("Wrong password");

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(AdoptionNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleAdoptionNotFound(AdoptionNotFoundException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());

    problemDetail.setType(URI.create(ENTITY_NOT_FOUND_INFO_URL));
    problemDetail.setTitle("Adoption not found");

    return ResponseEntity.status(404).body(problemDetail);
  }

  @ExceptionHandler(GuardianNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleGuardianNotFound(GuardianNotFoundException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());

    problemDetail.setType(URI.create(ENTITY_NOT_FOUND_INFO_URL));
    problemDetail.setTitle("Guardian not found");

    return ResponseEntity.status(404).body(problemDetail);
  }

  @ExceptionHandler(PetNotFoundException.class)
  public ResponseEntity<ProblemDetail> handlePetNotFound(PetNotFoundException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());

    problemDetail.setType(URI.create(ENTITY_NOT_FOUND_INFO_URL));
    problemDetail.setTitle("Pet not found");

    return ResponseEntity.status(404).body(problemDetail);
  }

  @ExceptionHandler(ShelterNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleShelterNotFound(ShelterNotFoundException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(404), exception.getMessage());

    problemDetail.setType(URI.create(ENTITY_NOT_FOUND_INFO_URL));
    problemDetail.setTitle("Shelter not found");

    return ResponseEntity.status(404).body(problemDetail);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ProblemDetail> handleAllUncaughtException(RuntimeException exception) {
    ProblemDetail problemDetail = ProblemDetail
        .forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());

    problemDetail.setType(URI.create("https://github.com/Edson-Mendes/adopet-api/problem-detail/internal-server-error"));
    problemDetail.setTitle("Something unexpected happened");

    return ResponseEntity.internalServerError().body(problemDetail);
  }

}
