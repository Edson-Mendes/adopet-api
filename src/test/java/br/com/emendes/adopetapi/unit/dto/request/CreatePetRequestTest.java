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
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .age("3 anos")
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "age");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate age must return violations when age is blank")
    void validateAge_MustReturnViolations_WhenAgeIsBlank(String blankAge) {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .age(blankAge)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "age");

      List<String> actualMessages = actualViolations.stream().map(ConstraintViolation::getMessage).toList();

      Assertions.assertThat(actualViolations).isNotEmpty();
      Assertions.assertThat(actualMessages).contains("age must not be blank");
    }

    @Test
    @DisplayName("Validate age must return violations when age contains more than 100 characters")
    void validateAge_MustReturnViolations_WhenAgeContainsMoreThan50Characters() {
      String ageWithMoreThan50Characters = "Um filhote com 3 meses!!!Um filhote com 3 meses!!!!";

      Assertions.assertThat(ageWithMoreThan50Characters).hasSizeGreaterThan(50);

      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .age(ageWithMoreThan50Characters)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "age");

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
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .image("http://www.xptopetsimages.com/images/d823hd9h0h08dhahcpcqpok019k84773dbsab")
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator.validateProperty(
          createPetRequest, "image");

      Assertions.assertThat(actualViolations).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("Validate image must return violations when image is blank")
    void validateImage_MustReturnViolations_WhenImageIsBlank(String blankImage) {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .image(blankImage)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator
          .validateProperty(createPetRequest, "image");

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

      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .image(imageWith256Characters)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator.validateProperty(
          createPetRequest, "image");

      Assertions.assertThat(imageWith256Characters).hasSize(256);
      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("image must contain max 255 characters");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http", "://www.xptocreatePets.com"})
    @DisplayName("Validate image must return violations when image not is a well-formed image")
    void validateImage_MustReturnViolations_WhenImageNotIsAWellFormedImage(String invalidImage) {
      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .image(invalidImage)
          .build();

      Set<ConstraintViolation<CreatePetRequest>> actualViolations = validator.validateProperty(
          createPetRequest, "image");

      Assertions.assertThat(actualViolations).isNotEmpty().hasSize(1);
      Assertions.assertThat(actualViolations.stream().findFirst().get().getMessage())
          .isEqualTo("image must be a well-formed url");
    }

  }

}