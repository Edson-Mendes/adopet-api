package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.PetController;
import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {PetController.class})
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
              "shelterId" : 1000
            }
          """;

      String actualContent = mockMvc.perform(post(PET_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated())
          .andReturn().getResponse().getContentAsString();

      PetResponse actualPetResponse = mapper.readValue(actualContent, PetResponse.class);

      Assertions.assertThat(actualPetResponse).isNotNull();
      Assertions.assertThat(actualPetResponse.getId()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualPetResponse.getName()).isNotNull().isEqualTo("Dark");
      Assertions.assertThat(actualPetResponse.getDescription()).isNotNull().isEqualTo("A very calm and cute cat");
      Assertions.assertThat(actualPetResponse.getAge()).isNotNull().isEqualTo("2 years old");
      Assertions.assertThat(actualPetResponse.getShelterId()).isNotNull().isEqualTo(1_000L);
    }

    @Test
    @DisplayName("Create must return status 400 and ProblemDetail when request body has invalid fields")
    void create_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "name" : "",
              "description" : "A very calm and cute cat",
              "age" : "2 years old",
              "shelterId" : 1000
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

}