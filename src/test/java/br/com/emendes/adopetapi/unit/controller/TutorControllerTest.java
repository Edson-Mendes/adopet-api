package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.TutorController;
import br.com.emendes.adopetapi.dto.request.CreateTutorRequest;
import br.com.emendes.adopetapi.dto.request.UpdateTutorRequest;
import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.TutorNotFoundException;
import br.com.emendes.adopetapi.service.TutorService;
import br.com.emendes.adopetapi.util.PageableResponse;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("Create must return status 201 and TutorResponse when create successfully")
    void create_MustReturnStatus201AndTutorResponse_WhenCreateSuccessfully() throws Exception {
      BDDMockito.when(tutorServiceMock.create(any(CreateTutorRequest.class)))
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
    @DisplayName("Create must return status 400 and ProblemDetail when passwords do not match")
    void create_MustReturnStatus400AndProblemDetail_WhenPasswordsDoNotMatch() throws Exception {
      BDDMockito.when(tutorServiceMock.create(any(CreateTutorRequest.class)))
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
    @DisplayName("Create must return status 400 and ProblemDetail when email already exists")
    void create_MustReturnStatus400AndProblemDetail_WhenEmailAlreadyExists() throws Exception {
      BDDMockito.when(tutorServiceMock.create(any(CreateTutorRequest.class)))
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
    @DisplayName("Create must return status 400 and ProblemDetail when request body has invalid fields")
    void create_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
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

  @Nested
  @DisplayName("Tests for update endpoint")
  class UpdateEndpoint {

    @Test
    @DisplayName("Update must return status 200 and TutorResponse when update successfully")
    void update_MustReturnStatus200AndTutorResponse_WhenUpdateSuccessfully() throws Exception {
      BDDMockito.when(tutorServiceMock.update(eq(100L), any(UpdateTutorRequest.class)))
          .thenReturn(updateTutorResponse());
      String requestBody = """
            {
              "name" : "Lorem Ipsum Dolor",
              "email" : "loremdolor@email.com"
            }
          """;

      String actualContent = mockMvc
          .perform(put(TUTOR_URI + "/100").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      TutorResponse actualTutorResponse = mapper.readValue(actualContent, TutorResponse.class);

      Assertions.assertThat(actualTutorResponse).isNotNull();
      Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum Dolor");
      Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("loremdolor@email.com");
    }

    @Test
    @DisplayName("Update must return status 400 and ProblemDetail when email already exists")
    void update_MustReturnStatus400AndProblemDetail_WhenEmailAlreadyExists() throws Exception {
      BDDMockito.when(tutorServiceMock.update(eq(100L), any(UpdateTutorRequest.class)))
          .thenThrow(new EmailAlreadyInUseException("E-mail {loremdolor@email.com} is already in use"));
      String requestBody = """
            {
              "name" : "Lorem Ipsum Dolor",
              "email" : "loremdolor@email.com"
            }
          """;

      String actualContent = mockMvc
          .perform(put(TUTOR_URI + "/100").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Email already in use");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("E-mail {loremdolor@email.com} is already in use");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Update must return status 400 and ProblemDetail when request body has invalid fields")
    void update_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "name" : "",
              "email" : "lorem@email.com"
            }
          """;

      String actualContent = mockMvc
          .perform(put(TUTOR_URI + "/100").contentType(CONTENT_TYPE).content(requestBody))
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

    @Test
    @DisplayName("Update must return status 404 and ProblemDetail when tutor not found")
    void update_MustReturnStatus404AndProblemDetail_WhenTutorNotFound() throws Exception {
      BDDMockito.when(tutorServiceMock.update(eq(100L), any(UpdateTutorRequest.class)))
          .thenThrow(new TutorNotFoundException("Tutor not found"));
      String requestBody = """
            {
              "name" : "Lorem Ipsum Dolor",
              "email" : "loremdolor@email.com"
            }
          """;

      String actualContent = mockMvc
          .perform(put(TUTOR_URI + "/100").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Tutor not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Tutor not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Update must return status 400 and ProblemDetail when given id is invalid")
    void update_MustReturnStatus400AndProblemDetail_WhenGivenIdIsInvalid() throws Exception {
      String requestBody = """
            {
              "name" : "Lorem Ipsum Dolor",
              "email" : "loremdolor@email.com"
            }
          """;

      String actualContent = mockMvc
          .perform(put(TUTOR_URI + "/1o0").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Type mismatch");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("An error occurred trying to cast String to Number");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

  }

  @Nested
  @DisplayName("Tests for find by id endpoint")
  class FindByIdEndpoint {

    @Test
    @DisplayName("FindById must return status 200 and TutorResponse when found successfully")
    void findById_MustReturnStatus200AndTutorResponse_WhenFoundSuccessfully() throws Exception {
      BDDMockito.when(tutorServiceMock.findById(100L))
          .thenReturn(tutorResponse());

      String actualContent = mockMvc.perform(get(TUTOR_URI + "/100"))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      TutorResponse actualTutorResponse = mapper.readValue(actualContent, TutorResponse.class);

      Assertions.assertThat(actualTutorResponse).isNotNull();
      Assertions.assertThat(actualTutorResponse.getId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualTutorResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualTutorResponse.getEmail()).isNotNull().isEqualTo("lorem@email.com");
    }

    @Test
    @DisplayName("FindById must return status 400 and ProblemDetail when id is invalid")
    void findById_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() throws Exception {
      String actualContent = mockMvc.perform(get(TUTOR_URI + "/1o0"))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Type mismatch");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("An error occurred trying to cast String to Number");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("FindById must return status 404 and ProblemDetail when tutor not found")
    void findById_MustReturnStatus404AndProblemDetail_WhenTutorNotFound() throws Exception {
      BDDMockito.when(tutorServiceMock.findById(100L))
          .thenThrow(new TutorNotFoundException("Tutor not found"));

      String actualContent = mockMvc
          .perform(get(TUTOR_URI + "/100"))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Tutor not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Tutor not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

  }

  @Nested
  @DisplayName("Tests for fetch all endpoint")
  class FetchAllEndpoint {

    @Test
    @DisplayName("fetchAll must return status 200 and Page<TutorResponse> when fetch successfully")
    void fetchAll_MustReturnStatus200AndPageTutorResponse_WhenFetchSuccessfully() throws Exception {
      BDDMockito.when(tutorServiceMock.fetchAll(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(tutorResponse()), PAGEABLE, 1));

      String actualContent = mockMvc.perform(get(TUTOR_URI))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      Page<TutorResponse> actualTutorResponsePage = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<TutorResponse>>() {});

      Assertions.assertThat(actualTutorResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

  @Nested
  @DisplayName("Tests for delete endpoint")
  class DeleteEndpoint {

    @Test
    @DisplayName("Delete must return status 204 when delete successfully")
    void delete_MustReturnStatus204_WhenDeleteSuccessfully() throws Exception {
      BDDMockito.doNothing().when(tutorServiceMock).deleteById(100L);

      mockMvc.perform(delete(TUTOR_URI+"/100"))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete must return status 400 and ProblemDetail when id is invalid")
    void delete_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() throws Exception {
      String actualContent = mockMvc.perform(delete(TUTOR_URI + "/1o0"))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Type mismatch");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("An error occurred trying to cast String to Number");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Delete must return status 404 and ProblemDetail when tutor do not exists")
    void delete_MustReturnStatus404AndProblemDetail_WhenTutorDoNotExists() throws Exception {
      BDDMockito.when(tutorServiceMock.deleteById(100L))
          .thenThrow(new TutorNotFoundException("Tutor not found"));

      String actualContent = mockMvc
          .perform(delete(TUTOR_URI + "/100"))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Tutor not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Tutor not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

  }

  private TutorResponse tutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .build();
  }

  private TutorResponse updateTutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum Dolor")
        .email("loremdolor@email.com")
        .build();
  }

}