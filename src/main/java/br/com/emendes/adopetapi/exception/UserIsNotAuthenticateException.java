package br.com.emendes.adopetapi.exception;

public class UserIsNotAuthenticateException extends RuntimeException {
  public UserIsNotAuthenticateException(String message) {
    super(message);
  }

}
