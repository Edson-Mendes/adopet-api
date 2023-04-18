package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.AdoptionController;
import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.exception.GuardianNotFoundException;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.service.AdoptionService;
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

import static br.com.emendes.adopetapi.util.AdoptionUtils.adoptionResponse;
import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static br.com.emendes.adopetapi.util.GuardianUtils.guardianResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {AdoptionController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
@DisplayName("Unit tests for AdoptionController")
class AdoptionControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private AdoptionService adoptionServiceMock;

  private static final String ADOPTION_URI = "/api/adoptions";
  private final String CONTENT_TYPE = "application/json;charset=UTF-8";

  @Nested
  @DisplayName("Tests for adopt endpoint")
  class AdoptEndpoint {

    @Test
    @DisplayName("Adopt must return status 201 and AdoptionResponse when adopt successfully")
    void adopt_MustReturnStatus201AndAdoptionResponse_WhenAdoptSuccessfully() throws Exception {
      BDDMockito.when(adoptionServiceMock.adopt(any(AdoptionRequest.class)))
          .thenReturn(adoptionResponse());
      String requestBody = """
            {
              "petId" : 10000
            }
          """;

      String actualContent = mockMvc.perform(post(ADOPTION_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isCreated())
          .andReturn().getResponse().getContentAsString();

      AdoptionResponse actualAdoptionResponse = mapper.readValue(actualContent, AdoptionResponse.class);

      Assertions.assertThat(actualAdoptionResponse).isNotNull();
      Assertions.assertThat(actualAdoptionResponse.getId()).isNotNull().isEqualTo(1_000_000L);
      Assertions.assertThat(actualAdoptionResponse.getPetId()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualAdoptionResponse.getGuardianId()).isNotNull().isEqualTo(100L);
    }

    @Test
    @DisplayName("Adopt must return status 400 and ProblemDetail when not found current user")
    void adopt_MustReturnStatus400AndProblemDetail_WhenNotFoundCurrentUser() throws Exception {
      BDDMockito.when(adoptionServiceMock.adopt(any(AdoptionRequest.class)))
          .thenThrow(new InvalidArgumentException("Current user not found"));
      String requestBody = """
            {
              "petId" : 10000
            }
          """;

      String actualContent = mockMvc.perform(post(ADOPTION_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Something went wrong");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull().isEqualTo("Current user not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Adopt must return status 400 and ProblemDetail when do not exists pet with id 10000")
    void adopt_MustReturnStatus400AndProblemDetail_WhenDoNotExistsPetWithId10000() throws Exception {
      BDDMockito.when(adoptionServiceMock.adopt(any(AdoptionRequest.class)))
          .thenThrow(new InvalidArgumentException("Invalid pet id"));
      String requestBody = """
            {
              "petId" : 10000
            }
          """;

      String actualContent = mockMvc.perform(post(ADOPTION_URI).contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Something went wrong");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull().isEqualTo("Invalid pet id");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

    @Test
    @DisplayName("Adopt must return status 400 and ProblemDetail when request body has invalid fields")
    void adopt_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "petId" : -1
            }
          """;

      String actualContent = mockMvc.perform(post(ADOPTION_URI).contentType(CONTENT_TYPE).content(requestBody))
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

      Assertions.assertThat(actualFields).isNotNull().contains("petId");
      Assertions.assertThat(actualMessages).isNotNull().contains("petId must be greater than zero");
    }

  }

  @Nested
  @DisplayName("Tests for fetch all endpoint")
  class FetchAllEndpoint {

    @Test
    @DisplayName("fetchAll must return status 200 and Page<AdoptionResponse> when fetch successfully")
    void fetchAll_MustReturnStatus200AndPageAdoptionResponse_WhenFetchSuccessfully() throws Exception {
      BDDMockito.when(adoptionServiceMock.fetchAll(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(adoptionResponse()), PAGEABLE, 1));

      String actualContent = mockMvc.perform(get(ADOPTION_URI))
          .andExpect(status().isOk())
          .andReturn().getResponse().getContentAsString();

      Page<GuardianResponse> actualGuardianResponsePage = mapper
          .readValue(actualContent, new TypeReference<PageableResponse<GuardianResponse>>() {
          });

      Assertions.assertThat(actualGuardianResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("fetchAll must return status 404 and ProblemDetail when Shelter not found")
    void fetchAll_MustReturnStatus404AndProblemDetail_WhenShelterNotFound() throws Exception {
      BDDMockito.when(adoptionServiceMock.fetchAll(PAGEABLE))
          .thenThrow(new ShelterNotFoundException("Shelter not found"));

      String actualContent = mockMvc.perform(get(ADOPTION_URI))
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
    @DisplayName("fetchAll must return status 404 and ProblemDetail when Guardian not found")
    void fetchAll_MustReturnStatus404AndProblemDetail_WhenGuardianNotFound() throws Exception {
      BDDMockito.when(adoptionServiceMock.fetchAll(PAGEABLE))
          .thenThrow(new GuardianNotFoundException("Guardian not found"));

      String actualContent = mockMvc.perform(get(ADOPTION_URI))
          .andExpect(status().isNotFound())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Guardian not found");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Guardian not found");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(404);
    }

  }

}