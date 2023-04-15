package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.AuthenticationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

@DisplayName("Unit tests for AuthenticationRequest")
class AuthenticationRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("Tests for email validation")
  class EmailValidation {

    @Test
    @DisplayName("Validate email must not return violations when email is valid")
    void validateEmail_MustNotReturnViolations_WhenEmailIsValid() {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .email("lorem@email.com")
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, "email");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate email must return violations when email is blank")
    void validateEmail_MustReturnViolations_WhenEmailIsBlank(String blankEmail) {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .email(blankEmail)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, "email");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("email must not be blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorememailcom", "lorem.com", "@email.com"})
    @DisplayName("Validate email must return violations when email is not well formed")
    void validateEmail_MustReturnViolations_WhenEmailIsNotWellFormed(String notWellFormedEmail) {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .email(notWellFormedEmail)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, "email");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("must be a well formed email");
    }

  }

  @Nested
  @DisplayName("Tests for password validation")
  class PasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890@#$%)(*&[]abcdefghijiedidbrf", "a"})
    @DisplayName("Validate password must not return violations when password is valid")
    void validatePassword_MustNotReturnViolations_WhenPasswordIsValid(String validPassword) {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .password(validPassword)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, "password");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate password must return violations when password is blank")
    void validatePassword_MustReturnViolations_WhenPasswordIsBlank(String blankPassword) {
      AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
          .password(blankPassword)
          .build();

      Set<ConstraintViolation<AuthenticationRequest>> actualViolations = validator
          .validateProperty(authenticationRequest, "password");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("password must not be blank");
    }

  }

}