package br.com.emendes.adopetapi.exception;

public class EmailAlreadyInUseException extends RuntimeException {

  public EmailAlreadyInUseException(String message) {
    super(message);
  }

}
