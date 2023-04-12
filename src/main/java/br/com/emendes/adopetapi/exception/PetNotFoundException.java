package br.com.emendes.adopetapi.exception;

public class PetNotFoundException extends RuntimeException {

  public PetNotFoundException(String message) {
    super(message);
  }

}
