package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.UpdateStatusRequest;
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

@DisplayName("Unit tests for UpdateStatusRequest")
class UpdateStatusRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Nested
    @DisplayName("Tests for status validation")
    class StatusValidation {

      @ParameterizedTest
      @ValueSource(strings = {
          "ANALYSING", "CONCLUDED", "CANCELED"
      })
      @DisplayName("Validate status must not return violations when status is valid")
      void validateStatus_MustNotReturnViolations_WhenStatusIsValid(String validStatus) {
        UpdateStatusRequest updateStatusRequest = UpdateStatusRequest.builder()
            .status(validStatus)
            .build();

        Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator
            .validateProperty(updateStatusRequest, "status");

        Assertions.assertThat(actualViolations).isEmpty();
      }

      @ParameterizedTest
      @NullAndEmptySource
      @ValueSource(strings = {"   ", "\t", "\n"})
      @DisplayName("Validate status must return violations when status is blank")
      void validateStatus_MustReturnViolations_WhenStatusIsBlank(String blankStatus) {
        UpdateStatusRequest updateStatusRequest = UpdateStatusRequest.builder()
            .status(blankStatus)
            .build();

        Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator
            .validateProperty(updateStatusRequest, "status");

        List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

        Assertions.assertThat(actualViolations).isNotEmpty();
        Assertions.assertThat(actualMessages).contains("status must not be blank");
      }

      @Test
      @DisplayName("Validate status must return violations when status do not match with AdoptionStatus")
      void validateStatus_MustReturnViolations_WhenStatusDoNotMatchWithAdoptionStatus() {
        UpdateStatusRequest updateStatusRequest = UpdateStatusRequest.builder()
            .status("xalala")
            .build();

        Set<ConstraintViolation<UpdateStatusRequest>> actualViolations = validator
            .validateProperty(updateStatusRequest, "status");

        List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

        Assertions.assertThat(actualViolations).isNotEmpty();
        Assertions.assertThat(actualMessages).contains("must be a valid status (ANALYSING, CANCELED and CONCLUDED)");
      }

    }

}