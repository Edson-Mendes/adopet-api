package br.com.emendes.adopetapi.exception;

public class IllegalOperationException extends RuntimeException {
  public IllegalOperationException(String message) {
    super(message);
  }
}
