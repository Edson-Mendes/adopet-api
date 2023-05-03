package br.com.emendes.adopetapi.validation.annotation;

import br.com.emendes.adopetapi.validation.validator.StatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A String anotada deve corresponder a um AdoptionStatus.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
public @interface ValidStatus {

  String message() default "must be a valid status (ANALYSING, CANCELED and CONCLUDED)";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

}
