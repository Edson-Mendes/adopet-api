package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.PetController;
import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.exception.PetNotFoundException;
import br.com.emendes.adopetapi.service.PetService;
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
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
import static br.com.emendes.adopetapi.util.PetUtils.petResponse;
import static br.com.emendes.adopetapi.util.PetUtils.updatedPetResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {PetController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
@DisplayName("Unit tests for PetController")
class PetControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private PetService petServiceMock;

  private static final String PET_URI = "/api/pets";

  @Nested
  @DisplayName("Tests for create endpoint")
  class CreateEndpoint {

    @Test
    @DisplayName("Create must return status 201 and PetResponse when create successfully")
    void create_MustReturnStatus201AndPetResponse_WhenCreateSuccessfully() throws Exception {
      BDDMockito.when(petServiceMock.create(any(CreatePetRequest.class)))
          .thenReturn(petResponse());
      String requestBody = """
            {
              "name" : "Dark",
              "description" : "A very calm and cute cat",
              "age" : "2 years old",
              "shelterId" : 1000,
              "image" : "http://www.xptopetimages/images/cat123"
            }
          """;

      String actualContent = mockMvc.perform(post(PET_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated())
          .andReturn().getResponse().getContentAsString();

      PetResponse actualPetResponse = mapper.readValue(actualContent, PetResponse.class);

      Assertions.assertThat(actualPetResponse).isNotNull();
      Assertions.assertThat(actualPetResponse.id()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualPetResponse.name()).isNotNull().isEqualTo("Dark");
      Assertions.assertThat(actualPetResponse.description()).isNotNull().isEqualTo("A very calm and cute cat");
      Assertions.assertThat(actualPetResponse.age()).isNotNull().isEqualTo("2 years old");
      Assertions.assertThat(actualPetResponse.shelterId()).isNotNull().isEqualTo(1_000L);
      Assertions.assertThat(actualPetResponse.images()).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("Create must return status 400 and ProblemDetail when request body has invalid fields")
    void create_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "name" : "",
              "description" : "A very calm and cute cat",
              "age" : "2 years old",
              "shelterId" : 1000,
              "image" : "http://www.xptopetimages/images/cat123"
            }
          """;

      String actualContent = mockMvc.perform(post(PET_URI).contentType(CONTENT_TYPE).content(requestBody))
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
  @DisplayName("Tests for fetch all endpoint")
  class FetchAllEndpoint {

    @Test
    @DisplayName("fetchAll must return status 200 and Page<PetResponse> when fetch successfully")
    void fetchAll_MustReturnStatus200AndPagePetResponseWhenFetchSuccessfully() throws Exception {
      BDDMockito.when(petServiceMock.fetchAll(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(petResponse()), PAGEABLE, 1));

      String actualContent = mockMvc.perform(get(PET_URI))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      Page<PetResponse> actualPetResponsePage = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<PetResponse>>() {
          });

      Assertions.assertThat(actualPetResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

  @Nested
  @DisplayName("Tests for find by id endpoint")
  class FindByIdEndpoint {

    @Test
    @DisplayName("FindById must return status 200 and PetResponse when found successfully")
    void findById_MustReturnStatus200AndPetResponse_WhenFoundSuccessfully() throws Exception {
      BDDMockito.when(petServiceMock.findById(10_000L))
          .thenReturn(petResponse());

      String actualContent = mockMvc.perform(get(PET_URI + "/10000"))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      PetResponse actualPetResponse = mapper.readValue(actualContent, PetResponse.class);

      Assertions.assertThat(actualPetResponse).isNotNull();
      Assertions.assertThat(actualPetResponse.id()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualPetResponse.name()).isNotNull().isEqualTo("Dark");
      Assertions.assertThat(actualPetResponse.description()).isNotNull().isEqualTo("A very calm and cute cat");
    }

    @Test
    @DisplayName("FindById must return status 400 and ProblemDetail when id is invalid")
    void findById_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() throws Exception {
      String actualContent = mockMvc.perform(get(PET_URI + "/1o0"))
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
    @DisplayName("FindById must return status 404 and ProblemDetail when pet not found")
    void findById_MustReturnStatus404AndProblemDetail_WhenPetNotFound() throws Exception {
      BDDMockito.when(petServiceMock.findById(10_000L))
          .thenThrow(new PetNotFoundException("Pet not found"));

      String actualContent = mockMvc
          .perform(get(PET_URI + "/10000"))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Pet not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Pet not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

  }

  @Nested
  @DisplayName("Tests for update endpoint")
  class UpdateEndpoint {

    @Test
    @DisplayName("Update must return status 200 and PetResponse when update successfully")
    void update_MustReturnStatus200AndPetResponse_WhenUpdateSuccessfully() throws Exception {
      BDDMockito.when(petServiceMock.update(eq(10_000L), any(UpdatePetRequest.class)))
          .thenReturn(updatedPetResponse());
      String requestBody = """
            {
              "name" : "Darkness",
              "description" : "A very cute cat",
              "age" : "3 years old"
            }
          """;

      String actualContent = mockMvc
          .perform(put(PET_URI + "/10000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      PetResponse actualPetResponse = mapper.readValue(actualContent, PetResponse.class);

      Assertions.assertThat(actualPetResponse).isNotNull();
      Assertions.assertThat(actualPetResponse.id()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualPetResponse.name()).isNotNull().isEqualTo("Darkness");
      Assertions.assertThat(actualPetResponse.description()).isNotNull().isEqualTo("A very cute cat");
      Assertions.assertThat(actualPetResponse.age()).isNotNull().isEqualTo("3 years old");
    }

    @Test
    @DisplayName("Update must return status 400 and ProblemDetail when request body has invalid fields")
    void update_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "name" : "",
              "description" : "A very cute cat",
              "age" : "3 years old"
            }
          """;

      String actualContent = mockMvc
          .perform(put(PET_URI + "/1000").contentType(CONTENT_TYPE).content(requestBody))
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
    @DisplayName("Update must return status 404 and ProblemDetail when pet not found")
    void update_MustReturnStatus404AndProblemDetail_WhenPetNotFound() throws Exception {
      BDDMockito.when(petServiceMock.update(eq(10_000L), any(UpdatePetRequest.class)))
          .thenThrow(new PetNotFoundException("Pet not found"));
      String requestBody = """
            {
              "name" : "Darkness",
              "description" : "A very cute cat",
              "age" : "3 years old"
            }
          """;

      String actualContent = mockMvc
          .perform(put(PET_URI + "/10000").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Pet not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Pet not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

    @Test
    @DisplayName("Update must return status 400 and ProblemDetail when given id is invalid")
    void update_MustReturnStatus400AndProblemDetail_WhenGivenIdIsInvalid() throws Exception {
      String requestBody = """
            {
              "name" : "Darkness",
              "description" : "A very cute cat",
              "age" : "3 years old"
            }
          """;

      String actualContent = mockMvc
          .perform(put(PET_URI + "/1o000").contentType(CONTENT_TYPE).content(requestBody))
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
  @DisplayName("Tests for delete endpoint")
  class DeleteEndpoint {

    @Test
    @DisplayName("Delete must return status 204 when delete successfully")
    void delete_MustReturnStatus204_WhenDeleteSuccessfully() throws Exception {
      BDDMockito.doNothing().when(petServiceMock).deleteById(1000L);

      mockMvc.perform(delete(PET_URI + "/1000"))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete must return status 400 and ProblemDetail when id is invalid")
    void delete_MustReturnStatus400AndProblemDetail_WhenIdIsInvalid() throws Exception {
      String actualContent = mockMvc.perform(delete(PET_URI + "/1o00"))
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
    @DisplayName("Delete must return status 404 and ProblemDetail when pet do not exists")
    void delete_MustReturnStatus404AndProblemDetail_WhenPetDoNotExists() throws Exception {
      BDDMockito.willThrow(new PetNotFoundException("Pet not found"))
          .given(petServiceMock).deleteById(1000L);

      String actualContent = mockMvc
          .perform(delete(PET_URI + "/1000"))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Pet not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Pet not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

  }

}