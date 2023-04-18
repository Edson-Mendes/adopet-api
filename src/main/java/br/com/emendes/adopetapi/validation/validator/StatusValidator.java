package br.com.emendes.adopetapi.validation.validator;

import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.validation.annotation.ValidStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return false;
    try {
      AdoptionStatus.valueOf(value);
      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

}
