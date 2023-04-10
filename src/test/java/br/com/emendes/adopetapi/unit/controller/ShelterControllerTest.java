package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.ShelterController;
import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.service.AnimalShelterService;
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

import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static br.com.emendes.adopetapi.util.ShelterUtils.animalShelterResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
  private AnimalShelterService animalShelterServiceMock;

  private static final String SHELTER_URI = "/api/shelters";

  @Nested
  @DisplayName("Tests for create endpoint")
  class CreateEndpoint {

    @Test
    @DisplayName("Create must return status 201 and ShelterResponse when create successfully")
    void create_MustReturnStatus201AndShelterResponse_WhenCreateSuccessfully() throws Exception {
      BDDMockito.when(animalShelterServiceMock.create(any(AnimalShelterRequest.class)))
          .thenReturn(animalShelterResponse());
      String requestBody = """
            {
              "name" : "Animal Shelter"
            }
          """;

      String actualContent = mockMvc.perform(post(SHELTER_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated())
          .andReturn().getResponse().getContentAsString();

      AnimalShelterResponse actualAnimalShelterResponse = mapper.readValue(actualContent, AnimalShelterResponse.class);

      Assertions.assertThat(actualAnimalShelterResponse).isNotNull();
      Assertions.assertThat(actualAnimalShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualAnimalShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
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

}