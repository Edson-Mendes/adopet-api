package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
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

@DisplayName("Unit tests for CreatePetRequest")
class CreatePetRequestTest {

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
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "name");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate name must return violations when name is blank")
    void validateName_MustReturnViolations_WhenNameIsBlank(String blankName) {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .name(blankName)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must not be blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name contains less than 2 characters")
    void validateName_MustReturnViolations_WhenNameContainsLessThan2Characters() {
      String nameWithLessThan2Characters = "l";

      Assertions.assertThat(nameWithLessThan2Characters).hasSizeLessThan(2);

      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .name(nameWithLessThan2Characters)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "name");

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

      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .name(nameWithMoreThan100Characters)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must contain between 2 and 100 characters");
    }

  }

  @Nested
  @DisplayName("Tests for description validation")
  class DescriptionValidation {

    @Test
    @DisplayName("Validate description must not return violations when description is valid")
    void validateDescription_MustNotReturnViolations_WhenDescriptionIsValid() {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .description("A very calm and cute cat")
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "description");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate description must return violations when description is blank")
    void validateDescription_MustReturnViolations_WhenDescriptionIsBlank(String blankDescription) {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .description(blankDescription)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "description");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("description must not be blank");
    }

    @Test
    @DisplayName("Validate description must return violations when description contains more than 255 characters")
    void validateDescription_MustReturnViolations_WhenDescriptionContainsMoreThan100Characters() {
      String descriptionWithMoreThan255Characters = "A very calm and cute cat!A very calm and cute cat!" +
          "A very calm and cute cat!A very calm and cute cat!A very calm and cute cat!A very calm and cute cat!" +
          "A very calm and cute cat!A very calm and cute cat!A very calm and cute cat!A very calm and cute cat!!!!!!!";

      Assertions.assertThat(descriptionWithMoreThan255Characters).hasSizeGreaterThan(255);

      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .description(descriptionWithMoreThan255Characters)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "description");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("description must contain max 255 characters");
    }

  }

  @Nested
  @DisplayName("Tests for age validation")
  class AgeValidation {

    @Test
    @DisplayName("Validate age must not return violations when age is valid")
    void validateAge_MustNotReturnViolations_WhenAgeIsValid() {
      short age = 1;
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .age(age)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "age");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate age must return violations when age is null")
    void validateAge_MustReturnViolations_WhenAgeIsNull() {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .age(null)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "age");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("age must not be null");
    }

    @Test
    @DisplayName("Validate age must return violations when age is negative")
    void validateAge_MustReturnViolations_WhenAgeIsNegative() {
      short age = -1;
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .age(age)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "age");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("age must be equal to or greater than zero");
    }

  }

  @Nested
  @DisplayName("Tests for shelterId validation")
  class ShelterIdValidation {

    @Test
    @DisplayName("Validate shelterId must not return violations when shelterId is valid")
    void validateShelterId_MustNotReturnViolations_WhenShelterIdIsValid() {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .shelterId(1000L)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "shelterId");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @Test
    @DisplayName("Validate shelterId must return violations when shelterId is null")
    void validateShelterId_MustReturnViolations_WhenShelterIdIsNull() {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .shelterId(null)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "shelterId");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("shelterId must not be null");
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1L})
    @DisplayName("Validate shelterId must return violations when shelterId is less than 1")
    void validateShelterId_MustReturnViolations_WhenShelterIdIsLessThan1(Long invalidShelterId) {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .shelterId(invalidShelterId)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "shelterId");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("shelterId must be greater than zero");
    }

  }

}