package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
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

@DisplayName("Unit tests for UpdateTutorRequest")
class UpdateTutorRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("Tests for name validation")
  class NameValidation {

    @ParameterizedTest
    @ValueSource(strings = {
        "Lorem Ipsum", "Du",
        "Name With 100 Characters!Name With 100 Characters!Name With 100 Characters!Name With 100 Characters!"
    })
    @DisplayName("Validate name must not return violations when name is valid")
    void validateName_MustNotReturnViolations_WhenNameIsValid(String validName) {
      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "name");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate name must return violations when name is blank")
    void validateName_MustReturnViolations_WhenNameIsBlank(String blankName) {
      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .name(blankName)
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must not be blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name contains less than 2 characters")
    void validateName_MustReturnViolations_WhenNameContainsLessThan2Characters() {
      String nameWithLessThan2Characters = "l";

      Assertions.assertThat(nameWithLessThan2Characters).hasSizeLessThan(2);

      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .name(nameWithLessThan2Characters)
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must contain between 2 and 100 characters");
    }

    @Test
    @DisplayName("Validate name must return violations when name contains more than 100 characters")
    void validateName_MustReturnViolations_WhenNameContainsMoreThan100Characters() {
      String nameWithMoreThan100Characters = "loremloremloremloremloremloremloremloremloremlorem" +
          "loremloremloremloremloremloremloremloremloremloreml";

      Assertions.assertThat(nameWithMoreThan100Characters).hasSizeGreaterThan(100);

      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .name(nameWithMoreThan100Characters)
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must contain between 2 and 100 characters");
    }

  }

  @Nested
  @DisplayName("Tests for email validation")
  class EmailValidation {

    @Test
    @DisplayName("Validate email must not return violations when email is valid")
    void validateEmail_MustNotReturnViolations_WhenEmailIsValid() {
      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .email("lorem@email.com")
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "email");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate email must return violations when email is blank")
    void validateEmail_MustReturnViolations_WhenEmailIsBlank(String blankEmail) {
      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .email(blankEmail)
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "email");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("email must not be blank");
    }

    @Test
    @DisplayName("Validate email must return violations when email contains more than 255 characters")
    void validateEmail_MustReturnViolations_WhenEmailContainsMoreThan100Characters() {
      String emailWithMoreThan255Characters = "loremloremloremloremloremloremloremloremloremlorem" +
          "loremloremloremloremloremloremloremloremloremlorem" +
          "loremloremloremloremloremloremloremloremloremlorem" +
          "loremloremloremloremloremloremloremloremloremlorem" +
          "loremloremloremloremloremloremloremloremloremlorem" +
          "@email.com";

      Assertions.assertThat(emailWithMoreThan255Characters).hasSizeGreaterThan(255);

      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .email(emailWithMoreThan255Characters)
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "email");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("email must contain max 255 characters");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorememailcom", "lorem.com", "@email.com"})
    @DisplayName("Validate email must return violations when email is not well formed")
    void validateEmail_MustReturnViolations_WhenEmailIsNotWellFormed(String notWellFormedEmail) {
      UpdateTutorRequest updateTutorRequest = UpdateTutorRequest.builder()
          .email(notWellFormedEmail)
          .build();

      Set<ConstraintViolation<UpdateTutorRequest>> actualViolations = validator
          .validateProperty(updateTutorRequest, "email");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("must be a well formed email");
    }

  }

}