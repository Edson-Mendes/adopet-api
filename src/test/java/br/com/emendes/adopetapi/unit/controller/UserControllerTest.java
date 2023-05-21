package br.com.emendes.adopetapi.unit.controller;

import br.com.emendes.adopetapi.controller.UserController;
import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.WrongPasswordException;
import br.com.emendes.adopetapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.emendes.adopetapi.util.ConstantUtils.CONTENT_TYPE;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
@DisplayName("Unit tests for UserController")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private UserService userServiceMock;

  private static final String USER_URI = "/api/users";

  @Nested
  @DisplayName("Tests for update endpoint")
  class UpdateEndpoint {

    @Test
    @DisplayName("Update password must return status 204 when update password successfully")
    void updatePassword_MustReturnStatus204_WhenUpdatePasswordSuccessfully() throws Exception {
      BDDMockito.doNothing().when(userServiceMock).updatePassword(any(UpdatePasswordRequest.class));
      String requestBody = """
            {
              "oldPassword" : "1234567890",
              "newPassword" : "12345678abcd",
              "confirmPassword" : "12345678abcd"
            }
          """;

      mockMvc.perform(patch(USER_URI + "/password").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Update password must return status 400 and ProblemDetail when request body has invalid fields")
    void updatePassword_MustReturnStatus400AndProblemDetail_WhenRequestBodyHasInvalidFields() throws Exception {
      String requestBody = """
            {
              "oldPassword" : "",
              "confirmPassword" : "12345678abcd"
            }
          """;

      String actualContent = mockMvc
          .perform(patch(USER_URI + "/password").contentType(CONTENT_TYPE).content(requestBody))
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

      Assertions.assertThat(actualFields).isNotNull().contains("oldPassword", "newPassword");
      Assertions.assertThat(actualMessages).isNotNull()
          .contains("oldPassword must not be blank", "newPassword must not be blank");
    }

    @Test
    @DisplayName("Update password must return status 400 and ProblemDetail when newPassword and confirmPassword does not match")
    void updatePassword_MustReturnStatus400AndProblemDetail_WhenNewPasswordAndConfirmPasswordDoesNotMatch() throws Exception {
      BDDMockito.willThrow(new PasswordsDoNotMatchException("Passwords do not match"))
          .given(userServiceMock).updatePassword(any(UpdatePasswordRequest.class));

      String requestBody = """
            {
              "oldPassword" : "1234567890",
              "newPassword" : "12345678abcd",
              "confirmPassword" : "12345678ab"
            }
          """;

      String actualContent = mockMvc
          .perform(patch(USER_URI + "/password").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Passwords do not match");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Passwords do not match");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }


    @Test
    @DisplayName("Update password must return status 400 and ProblemDetail when oldPassword is wrong")
    void updatePassword_MustReturnStatus400AndProblemDetail_WhenOldPasswordIsWrong() throws Exception {
      BDDMockito.willThrow(new WrongPasswordException("Old password is wrong"))
          .given(userServiceMock).updatePassword(any(UpdatePasswordRequest.class));

      String requestBody = """
            {
              "oldPassword" : "123456789",
              "newPassword" : "12345678abcd",
              "confirmPassword" : "12345678abcd"
            }
          """;

      String actualContent = mockMvc
          .perform(patch(USER_URI + "/password").contentType(CONTENT_TYPE).content(requestBody))
          .andExpect(status().isBadRequest())
          .andReturn().getResponse().getContentAsString();

      ProblemDetail actualProblemDetail = mapper.readValue(actualContent, ProblemDetail.class);

      Assertions.assertThat(actualProblemDetail).isNotNull();
      Assertions.assertThat(actualProblemDetail.getTitle()).isNotNull().isEqualTo("Wrong password");
      Assertions.assertThat(actualProblemDetail.getDetail()).isNotNull()
          .isEqualTo("Old password is wrong");
      Assertions.assertThat(actualProblemDetail.getStatus()).isEqualTo(400);
    }

  }

}
