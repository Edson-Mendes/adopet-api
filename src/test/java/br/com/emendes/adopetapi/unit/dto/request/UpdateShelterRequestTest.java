package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
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

@DisplayName("Unit tests for UpdateShelterRequest")
class UpdateShelterRequestTest {

  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Nested
  @DisplayName("Tests for name validation")
  class NameValidation {

    @ParameterizedTest
    @ValueSource(strings = {
        "Animal Shelter", "Du",
        "Name With 100 Characters!Name With 100 Characters!Name With 100 Characters!Name With 100 Characters!"
    })
    @DisplayName("Validate name must not return violations when name is valid")
    void validateName_MustNotReturnViolations_WhenNameIsValid(String validName) {
      UpdateShelterRequest updateShelterRequest = UpdateShelterRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<UpdateShelterRequest>> actualViolations = validator
          .validateProperty(updateShelterRequest, "name");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate name must return violations when name is blank")
    void validateName_MustReturnViolations_WhenNameIsBlank(String blankName) {
      UpdateShelterRequest updateShelterRequest = UpdateShelterRequest.builder()
          .name(blankName)
          .build();

      Set<ConstraintViolation<UpdateShelterRequest>> actualViolations = validator
          .validateProperty(updateShelterRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must not be blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name contains less than 2 characters")
    void validateName_MustReturnViolations_WhenNameContainsLessThan2Characters() {
      String nameWithLessThan2Characters = "l";

      Assertions.assertThat(nameWithLessThan2Characters).hasSizeLessThan(2);

      UpdateShelterRequest updateShelterRequest = UpdateShelterRequest.builder()
          .name(nameWithLessThan2Characters)
          .build();

      Set<ConstraintViolation<UpdateShelterRequest>> actualViolations = validator
          .validateProperty(updateShelterRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must contain between 2 and 100 characters");
    }

    @Test
    @DisplayName("Validate name must return violations when name contains more than 100 characters")
    void validateName_MustReturnViolations_WhenNameContainsMoreThan100Characters() {
      String nameWithMoreThan100Characters = "sheltersheltersheltersheltersheltersheltershelter"+
          "sheltersheltersheltersheltersheltersheltershelter___";

      Assertions.assertThat(nameWithMoreThan100Characters).hasSizeGreaterThan(100);

      UpdateShelterRequest updateShelterRequest = UpdateShelterRequest.builder()
          .name(nameWithMoreThan100Characters)
          .build();

      Set<ConstraintViolation<UpdateShelterRequest>> actualViolations = validator
          .validateProperty(updateShelterRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must contain between 2 and 100 characters");
    }

  }

}