package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
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

@DisplayName("Unit tests for TutorRequest")
class CreateTutorRequestTest {

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
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "name");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate name must return violations when name is blank")
    void validateName_MustReturnViolations_WhenNameIsBlank(String blankName) {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .name(blankName)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must not be blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name contains less than 2 characters")
    void validateName_MustReturnViolations_WhenNameContainsLessThan2Characters() {
      String nameWithLessThan2Characters = "l";

      Assertions.assertThat(nameWithLessThan2Characters).hasSizeLessThan(2);

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .name(nameWithLessThan2Characters)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "name");

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

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .name(nameWithMoreThan100Characters)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "name");

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
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .email("lorem@email.com")
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "email");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate email must return violations when email is blank")
    void validateEmail_MustReturnViolations_WhenEmailIsBlank(String blankEmail) {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .email(blankEmail)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "email");

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

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .email(emailWithMoreThan255Characters)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "email");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("email must contain max 255 characters");
    }

    @ParameterizedTest
    @ValueSource(strings = {"lorememailcom", "lorem.com", "@email.com"})
    @DisplayName("Validate email must return violations when email is not well formed")
    void validateEmail_MustReturnViolations_WhenEmailIsNotWellFormed(String notWellFormedEmail) {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .email(notWellFormedEmail)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "email");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("must be a well formed email");
    }

  }

  @Nested
  @DisplayName("Tests for password validation")
  class PasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890@#$%)(*&[]abcdefghij"})
    @DisplayName("Validate password must not return violations when password is valid")
    void validatePassword_MustNotReturnViolations_WhenPasswordIsValid(String validPassword) {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .password(validPassword)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "password");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate password must return violations when password is blank")
    void validatePassword_MustReturnViolations_WhenPasswordIsBlank(String blankPassword) {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .password(blankPassword)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "password");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("password must not be blank");
    }

    @Test
    @DisplayName("Validate password must return violations when password contains less than 7 characters")
    void validatePassword_MustReturnViolations_WhenPasswordContainsLessThan2Characters() {
      String passwordWithLessThan8Characters = "1234567";

      Assertions.assertThat(passwordWithLessThan8Characters).hasSizeLessThan(8);

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .password(passwordWithLessThan8Characters)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "password");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("password must contain between 8 and 30 characters");
    }

    @Test
    @DisplayName("Validate password must return violations when password contains more than 30 characters")
    void validatePassword_MustReturnViolations_WhenPasswordContainsMoreThan100Characters() {
      String passwordWithMoreThan30Characters = "123456789012345678901234567890_";

      Assertions.assertThat(passwordWithMoreThan30Characters).hasSizeGreaterThan(30);

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .password(passwordWithMoreThan30Characters)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "password");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("password must contain between 8 and 30 characters");
    }

  }

  @Nested
  @DisplayName("Tests for confirmPassword validation")
  class ConfirmPasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890@#$%)(*&[]abcdefghij"})
    @DisplayName("Validate confirmPassword must not return violations when confirmPassword is valid")
    void validateConfirmPassword_MustNotReturnViolations_WhenConfirmPasswordIsValid(String validPassword) {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .confirmPassword(validPassword)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "confirmPassword");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate confirmPassword must return violations when confirmPassword is blank")
    void validateConfirmPassword_MustReturnViolations_WhenConfirmPasswordIsBlank(String blankConfirmPassword) {
      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .confirmPassword(blankConfirmPassword)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "confirmPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("confirmPassword must not be blank");
    }

    @Test
    @DisplayName("Validate confirmPassword must return violations when confirmPassword contains less than 7 characters")
    void validateConfirmPassword_MustReturnViolations_WhenConfirmPasswordContainsLessThan2Characters() {
      String confirmPasswordWithLessThan8Characters = "1234567";

      Assertions.assertThat(confirmPasswordWithLessThan8Characters).hasSizeLessThan(8);

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .confirmPassword(confirmPasswordWithLessThan8Characters)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "confirmPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("confirmPassword must contain between 8 and 30 characters");
    }

    @Test
    @DisplayName("Validate confirmPassword must return violations when confirmPassword contains more than 30 characters")
    void validateConfirmPassword_MustReturnViolations_WhenConfirmPasswordContainsMoreThan100Characters() {
      String confirmPasswordWithMoreThan30Characters = "123456789012345678901234567890_";

      Assertions.assertThat(confirmPasswordWithMoreThan30Characters).hasSizeGreaterThan(30);

      CreateTutorRequest createTutorRequest = CreateTutorRequest.builder()
          .confirmPassword(confirmPasswordWithMoreThan30Characters)
          .build();

      Set<ConstraintViolation<CreateTutorRequest>> actualViolations = validator
          .validateProperty(createTutorRequest, "confirmPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("confirmPassword must contain between 8 and 30 characters");
    }

  }

}