package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Set;

@DisplayName("Unit tests for AdoptionRequest")
class AdoptionRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("Tests for petId validation")
  class PetIdValidation {

    @Test
    @DisplayName("Validate petId must not return violations when petId is valid")
    void validatePetId_MustNotReturnViolations_WhenPetIdIsValid() {
      AdoptionRequest adoptionRequest = AdoptionRequest.builder()
          .petId(1000L)
          .build();

      Set<ConstraintViolation<AdoptionRequest>> actualViolations = validator
          .validateProperty(adoptionRequest, "petId");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate petId must return violations when petId is null")
    void validatePetId_MustReturnViolations_WhenPetIdIsNull() {
      AdoptionRequest adoptionRequest = AdoptionRequest.builder()
          .petId(null)
          .build();

      Set<ConstraintViolation<AdoptionRequest>> actualViolations = validator
          .validateProperty(adoptionRequest, "petId");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("petId must not be null");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1L})
    @DisplayName("Validate petId must return violations when petId is less than 1")
    void validatePetId_MustReturnViolations_WhenPetIdIsLessThan1(Long invalidPetId) {
      AdoptionRequest adoptionRequest = AdoptionRequest.builder()
          .petId(invalidPetId)
          .build();

      Set<ConstraintViolation<AdoptionRequest>> actualViolations = validator
          .validateProperty(adoptionRequest, "petId");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("petId must be greater than zero");
    }

  }

}