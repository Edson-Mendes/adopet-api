package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;
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

@DisplayName("Unit tests for class UpdatePasswordRequest")
class UpdatePasswordRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("Tests for oldPassword validation")
  class OldPasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890@#$%)(*&[]abcdefghij"})
    @DisplayName("Validate oldPassword must not return violations when oldPassword is valid")
    void validateOldPassword_MustNotReturnViolations_WhenOldPasswordIsValid(String validPassword) {
      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .oldPassword(validPassword)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "oldPassword");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate oldPassword must return violations when oldPassword is blank")
    void validateOldPassword_MustReturnViolations_WhenOldPasswordIsBlank(String blankOldPassword) {
      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .oldPassword(blankOldPassword)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "oldPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("oldPassword must not be blank");
    }

  }

  @Nested
  @DisplayName("Tests for newPassword validation")
  class NewPasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890@#$%)(*&[]abcdefghij"})
    @DisplayName("Validate newPassword must not return violations when newPassword is valid")
    void validateNewPassword_MustNotReturnViolations_WhenNewPasswordIsValid(String validNewPassword) {
      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .newPassword(validNewPassword)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "newPassword");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate newPassword must return violations when newPassword is blank")
    void validateNewPassword_MustReturnViolations_WhenNewPasswordIsBlank(String blankNewPassword) {
      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .newPassword(blankNewPassword)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "newPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("newPassword must not be blank");
    }

    @Test
    @DisplayName("Validate newPassword must return violations when newPassword contains less than 7 characters")
    void validateNewPassword_MustReturnViolations_WhenNewPasswordContainsLessThan2Characters() {
      String newPasswordWithLessThan8Characters = "1234567";

      Assertions.assertThat(newPasswordWithLessThan8Characters).hasSizeLessThan(8);

      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .newPassword(newPasswordWithLessThan8Characters)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "newPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("newPassword must contain between 8 and 30 characters");
    }

    @Test
    @DisplayName("Validate newPassword must return violations when newPassword contains more than 30 characters")
    void validateNewPassword_MustReturnViolations_WhenNewPasswordContainsMoreThan100Characters() {
      String newPasswordWithMoreThan30Characters = "123456789012345678901234567890_";

      Assertions.assertThat(newPasswordWithMoreThan30Characters).hasSizeGreaterThan(30);

      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .newPassword(newPasswordWithMoreThan30Characters)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "newPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("newPassword must contain between 8 and 30 characters");
    }

  }

  @Nested
  @DisplayName("Tests for confirmPassword validation")
  class ConfirmPasswordValidation {

    @ParameterizedTest
    @ValueSource(strings = {"12345678", "1234567890@#$%)(*&[]abcdefghij"})
    @DisplayName("Validate confirmPassword must not return violations when confirmPassword is valid")
    void validateConfirmPassword_MustNotReturnViolations_WhenConfirmPasswordIsValid(String validPassword) {
      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .confirmPassword(validPassword)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "confirmPassword");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate confirmPassword must return violations when confirmPassword is blank")
    void validateConfirmPassword_MustReturnViolations_WhenConfirmPasswordIsBlank(String blankConfirmPassword) {
      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .confirmPassword(blankConfirmPassword)
          .build();

      Set<ConstraintViolation<UpdatePasswordRequest>> actualViolations = validator
          .validateProperty(updatePasswordRequest, "confirmPassword");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("confirmPassword must not be blank");
    }

  }

}