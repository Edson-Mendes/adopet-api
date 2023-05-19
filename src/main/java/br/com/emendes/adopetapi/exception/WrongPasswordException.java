package br.com.emendes.adopetapi.exception;

public class WrongPasswordException extends RuntimeException {

  public WrongPasswordException(String message) {
    super(message);
  }

}
