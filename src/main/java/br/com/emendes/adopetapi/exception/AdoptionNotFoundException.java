package br.com.emendes.adopetapi.exception;

public class AdoptionNotFoundException extends RuntimeException {
  public AdoptionNotFoundException(String message) {
    super(message);
  }

}
