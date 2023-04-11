package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.ShelterController;
import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.service.ShelterService;
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

import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static br.com.emendes.adopetapi.util.ShelterUtils.shelterResponse;
import static br.com.emendes.adopetapi.util.ShelterUtils.updatedShelterResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {ShelterController.class})
@ActiveProfiles("test")
@DisplayName("Unit tests for ShelterController")
class ShelterControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private ShelterService shelterServiceMock;

  private static final String SHELTER_URI = "/api/shelters";

  @Nested
  @DisplayName("Tests for create endpoint")
  class CreateEndpoint {

    @Test
    @DisplayName("Create must return status 201 and ShelterResponse when create successfully")
    void create_MustReturnStatus201AndShelterResponse_WhenCreateSuccessfully() throws Exception {
      BDDMockito.when(shelterServiceMock.create(any(ShelterRequest.class)))
          .thenReturn(shelterResponse());
      String requestBody = """
            {
              "name" : "Animal Shelter"
            }
          """;

      String actualContent = mockMvc.perform(post(SHELTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated())
          .andReturn().getResponse().getContentAsString();

      ShelterResponse actualShelterResponse = mapper.readValue(actualContent, ShelterResponse.class);

      Assertions.assertThat(actualShelterResponse).isNotNull();
      Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
    }

    @Test
    @DisplayName("Create must return status 400 and ProblemDetail when request body has invalid fields")
    void create_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "name" : "a"
            }
          """;

      String actualContent = mockMvc.perform(post(SHELTER_URI).contentType(CONTENT_TYPE).content(requestBody))
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
      Assertions.assertThat(actualMessages).isNotNull().contains("name must contain between 2 and 100 characters");
    }

  }

  @Nested
  @DisplayName("Tests for fetch all endpoint")
  class FetchAllEndpoint {

    @Test
    @DisplayName("fetchAll must return status 200 and Page<ShelterResponse> when fetch successfully")
    void fetchAll_MustReturnStatus200AndPageShelterResponseWhenFetchSuccessfully() throws Exception {
      BDDMockito.when(shelterServiceMock.fetchAll(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(shelterResponse()), PAGEABLE, 1));

      String actualContent = mockMvc.perform(get(SHELTER_URI))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      Page<ShelterResponse> actualShelterResponsePage = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<ShelterResponse>>() {
          });

      Assertions.assertThat(actualShelterResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

  @Nested
  @DisplayName("Tests for find by id endpoint")
  class FindByIdEndpoint {

    @Test
    @DisplayName("FindById must return status 200 and ShelterResponse when found successfully")
    void findById_MustReturnStatus200AndShelterResponse_WhenFoundSuccessfully() throws Exception {
      BDDMockito.when(shelterServiceMock.findById(1000L))
          .thenReturn(shelterResponse());

      String actualContent = mockMvc.perform(get(SHELTER_URI + "/1000"))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      ShelterResponse actualShelterResponse = mapper.readValue(actualContent, ShelterResponse.class);

      Assertions.assertThat(actualShelterResponse).isNotNull();
      Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
    }

    @Test
    @DisplayName("FindById must return status 400 and ProblemDetail when id is invalid")
    void findById_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() throws Exception {
      String actualContent = mockMvc.perform(get(SHELTER_URI + "/1o0"))
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
    @DisplayName("FindById must return status 404 and ProblemDetail when shelter not found")
    void findById_MustReturnStatus404AndProblemDetail_WhenShelterNotFound() throws Exception {
      BDDMockito.when(shelterServiceMock.findById(1000L))
          .thenThrow(new ShelterNotFoundException("Shelter not found"));

      String actualContent = mockMvc
          .perform(get(SHELTER_URI + "/1000"))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Shelter not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Shelter not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

  }

  @Nested
  @DisplayName("Tests for update endpoint")
  class UpdateEndpoint {

    @Test
    @DisplayName("Update must return status 200 and ShelterResponse when update successfully")
    void update_MustReturnStatus200AndShelterResponse_WhenUpdateSuccessfully() throws Exception {
      BDDMockito.when(shelterServiceMock.update(eq(1000L), any(ShelterRequest.class)))
          .thenReturn(updatedShelterResponse());
      String requestBody = """
            {
              "name" : "Animal Shelter ABC"
            }
          """;

      String actualContent = mockMvc
          .perform(put(SHELTER_URI + "/1000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      ShelterResponse actualShelterResponse = mapper.readValue(actualContent, ShelterResponse.class);

      Assertions.assertThat(actualShelterResponse).isNotNull();
      Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter ABC");
    }

    @Test
    @DisplayName("Update must return status 400 and ProblemDetail when request body has invalid fields")
    void update_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "name" : ""
            }
          """;

      String actualContent = mockMvc
          .perform(put(SHELTER_URI + "/1000").contentType(CONTENT_TYPE).content(requestBody))
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
    @DisplayName("Update must return status 404 and ProblemDetail when shelter not found")
    void update_MustReturnStatus404AndProblemDetail_WhenShelterNotFound() throws Exception {
      BDDMockito.when(shelterServiceMock.update(eq(1000L), any(ShelterRequest.class)))
          .thenThrow(new ShelterNotFoundException("Shelter not found"));
      String requestBody = """
            {
              "name" : "Animal Shelter ABC"
            }
          """;

      String actualContent = mockMvc
          .perform(put(SHELTER_URI + "/1000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Shelter not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Shelter not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Update must return status 400 and ProblemDetail when given id is invalid")
    void update_MustReturnStatus400AndProblemDetail_WhenGivenIdIsInvalid() throws Exception {
      String requestBody = """
            {
              "name" : "Animal Shelter ABC"
            }
          """;

      String actualContent = mockMvc
          .perform(put(SHELTER_URI + "/1o00").contentType(CONTENT_TYPE).content(requestBody))
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

}