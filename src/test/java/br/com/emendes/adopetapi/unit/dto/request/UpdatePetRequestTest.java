package br.com.emendes.adopetapi.unit.dto.request;

import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
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

@DisplayName("Unit tests for UpdatePetRequest")
class UpdatePetRequestTest {

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
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .name(validName)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "name");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate name must return violations when name is blank")
    void validateName_MustReturnViolations_WhenNameIsBlank(String blankName) {
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .name(blankName)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "name");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("name must not be blank");
    }

    @Test
    @DisplayName("Validate name must return violations when name contains less than 2 characters")
    void validateName_MustReturnViolations_WhenNameContainsLessThan2Characters() {
      String nameWithLessThan2Characters = "l";

      Assertions.assertThat(nameWithLessThan2Characters).hasSizeLessThan(2);

      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .name(nameWithLessThan2Characters)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "name");

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

      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .name(nameWithMoreThan100Characters)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "name");

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
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .description("A very calm and cute cat")
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "description");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate description must return violations when description is blank")
    void validateDescription_MustReturnViolations_WhenDescriptionIsBlank(String blankDescription) {
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .description(blankDescription)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "description");

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

      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .description(descriptionWithMoreThan255Characters)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "description");

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
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .age("3 anos")
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "age");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate age must return violations when age is blank")
    void validateAge_MustReturnViolations_WhenAgeIsBlank(String blankAge) {
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .age(blankAge)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "age");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("age must not be blank");
    }

    @Test
    @DisplayName("Validate age must return violations when age contains more than 100 characters")
    void validateAge_MustReturnViolations_WhenAgeContainsMoreThan50Characters() {
      String ageWithMoreThan50Characters = "3 years old3 years old3 years old3 years old3 years old";

      Assertions.assertThat(ageWithMoreThan50Characters).hasSizeGreaterThan(50);

      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .age(ageWithMoreThan50Characters)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "age");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("age must contain between 1 and 50 characters");
    }

  }

  @Nested
  @DisplayName("Tests for image validation")
  class ImageValidation {

    @Test
    @DisplayName("Validate image must not return violations when image is valid")
    void validateImage_MustNotReturnViolations_WhenImageIsValid() {
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .image("http://www.xptopetsimages.com/images/d823hd9h0h08dhahcpcqpok019k84773dbsab")
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator.validateProperty(
          updatePetRequest, "image");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate image must return violations when image is blank")
    void validateImage_MustReturnViolations_WhenImageIsBlank(String blankImage) {
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .image(blankImage)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator
          .validateProperty(updatePetRequest, "image");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("image must not be blank");
    }

    @Test
    @DisplayName("Validate image must return violations when image size is bigger than 255 image")
    void validateImage_MustReturnViolations_WhenImageSizeIsBiggerThan100Characters() {
      String imageWith256Characters = "http://www.xptopetimages.com/images/76t768w67ge8a9hc" +
          "hhf976gds76g97sahgf98ncijcn2843729hfdbncinasnlkasf6dft87a6tds5asg8c76h87h029310u4foadncjanxkam9fawe43i" +
          "hhf976gds76g97sahgf98ncijcn2843729hfdbncinasnlkasf6dft87a6tds5asg8c76h87h029310u4foadncjanxkam9fawe43i";

      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .image(imageWith256Characters)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator.validateProperty(
          updatePetRequest, "image");

      Assertions.assertThat(imageWith256Characters).hasSize(256);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("image must contain max 255 characters");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http", "://www.xptocreatePets.com"})
    @DisplayName("Validate image must return violations when image not is a well-formed image")
    void validateImage_MustReturnViolations_WhenImageNotIsAWellFormedImage(String invalidImage) {
      UpdatePetRequest updatePetRequest = UpdatePetRequest.builder()
          .image(invalidImage)
          .build();

      Set<ConstraintViolation<UpdatePetRequest>> actualViolations = validator.validateProperty(
          updatePetRequest, "image");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("image must be a well-formed url");
    }

  }

}