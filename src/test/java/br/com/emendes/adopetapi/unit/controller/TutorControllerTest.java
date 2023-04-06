package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.TutorController;
import br.com.emendes.adopetapi.dto.request.TutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.service.TutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {TutorController.class})
@ActiveProfiles("test")
@DisplayName("Unit tests for TutorController")
class TutorControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private TutorService tutorServiceMock;

  private static final String TUTOR_URI = "/api/tutors";
  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Tests for create endpoint")
  class CreateEndpoint {

    @Test
    @DisplayName("Create must return TutorResponse when create successfully")
    void create_MustReturnTutorResponse_WhenCreateSuccessfully() throws Exception {
      BDDMockito.when(tutorServiceMock.create(any(TutorRequest.class)))
          .thenReturn(tutorResponse());
      String requestBody = """
            {
              "name" : "Lorem Ipsum",
              "email" : "lorem@email.com",
              "password" : "1234567890",
              "confirmPassword" : "1234567890"
            }
          """;

      String actualContent = mockMvc.perform(post(TUTOR_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated())
          .andReturn().getResponse().getContentAsString();

      TutorResponse actualTutorResponse = mapper.readValue(actualContent, TutorResponse.class);

      Assertions.assertThat(actualTutorResponse).isNotNull();
      Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("lorem@email.com");
    }

    @Test
    @DisplayName("Create must return ProblemDetail when passwords do not match")
    void create_MustReturnProblemDetail_WhenPasswordsDoNotMatch() throws Exception {
      BDDMockito.when(tutorServiceMock.create(any(TutorRequest.class)))
          .thenThrow(new PasswordsDoNotMatchException("Passwords do not match"));
      String requestBody = """
            {
              "name" : "Lorem Ipsum",
              "email" : "lorem@email.com",
              "password" : "1234567890",
              "confirmPassword" : "12345678"
            }
          """;

      String actualContent = mockMvc.perform(post(TUTOR_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Passwords do not match");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull().isEqualTo("Passwords do not match");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Create must return ProblemDetail when email already exists")
    void create_MustReturnProblemDetail_WhenEmailAlreadyExists() throws Exception {
      BDDMockito.when(tutorServiceMock.create(any(TutorRequest.class)))
          .thenThrow(new EmailAlreadyInUseException("E-mail {lorem@email.com} is already in use"));
      String requestBody = """
            {
              "name" : "Lorem Ipsum",
              "email" : "lorem@email.com",
              "password" : "1234567890",
              "confirmPassword" : "1234567890"
            }
          """;

      String actualContent = mockMvc.perform(post(TUTOR_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Email already in use");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("E-mail {lorem@email.com} is already in use");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Create must return ProblemDetail when request body has invalid fields")
    void create_MustReturnProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "name" : "",
              "email" : "lorem@email.com",
              "password" : "1234567890",
              "confirmPassword" : "1234567890"
            }
          """;

      String actualContent = mockMvc.perform(post(TUTOR_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Invalid arguments");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Some fields are invalid");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);

      Assertions.assertThat(actualProblemDetail.getProperties()).isNotNull();

      String actualFields = (String) actualProblemDetail.getProperties().get("fields");
      String actualMessages = (String) actualProblemDetail.getProperties().get("messages");

      Assertions.assertThat(actualFields).isNotNull().contains("name");
      Assertions.assertThat(actualMessages).isNotNull().contains("name must not be blank");
    }

  }

  private TutorResponse tutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .build();
  }

}