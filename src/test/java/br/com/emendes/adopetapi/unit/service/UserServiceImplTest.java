package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.UpdatePasswordRequest;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.WrongPasswordException;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.UserRepository;
import br.com.emendes.adopetapi.service.CurrentUserService;
import br.com.emendes.adopetapi.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.emendes.adopetapi.util.UserUtils.guardianUser;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for UserServiceImpl")
class UserServiceImplTest {

  @InjectMocks
  private UserServiceImpl userService;
  @Mock
  private CurrentUserService currentUserServiceMock;
  @Mock
  private PasswordEncoder passwordEncoderMock;
  @Mock
  private UserRepository userRepositoryMock;

  @Nested
  @DisplayName("Tests for updatePassword method")
  class UpdatePasswordMethod {

    @Test
    @DisplayName("Update Password must call UserRepository.save when update password successfully")
    void updatePassword_MustCallUserRepositorySave_WhenUpdatePasswordSuccessfully() {
      BDDMockito.when(currentUserServiceMock.getCurrentUser()).thenReturn(guardianUser());
      BDDMockito.when(passwordEncoderMock.matches(any(String.class), any(String.class))).thenReturn(true);
      BDDMockito.when(passwordEncoderMock.encode(any(String.class))).thenReturn("12345678");
      BDDMockito.when(userRepositoryMock.save(any(User.class))).thenReturn(guardianUser());

      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .oldPassword("1234567890")
          .newPassword("12345678")
          .confirmPassword("12345678")
          .build();

      userService.updatePassword(updatePasswordRequest);

      BDDMockito.verify(currentUserServiceMock).getCurrentUser();
      BDDMockito.verify(passwordEncoderMock).matches(any(String.class), any(String.class));
      BDDMockito.verify(passwordEncoderMock).encode(any(String.class));
      BDDMockito.verify(userRepositoryMock).save(any(User.class));
    }

    @Test
    @DisplayName("Update Password must throw PasswordsDoNotMatchException when newPassword and confirmPassword does not match")
    void updatePassword_MustThrowPasswordsDoNotMatchException_WhenNewPasswordAndConfirmPasswordDoesNotMatch() {
      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .oldPassword("1234567890")
          .newPassword("12345678")
          .confirmPassword("12345678000")
          .build();

      Assertions.assertThatExceptionOfType(PasswordsDoNotMatchException.class)
          .isThrownBy(() -> userService.updatePassword(updatePasswordRequest))
          .withMessage("Passwords do not match");
    }

    @Test
    @DisplayName("Update Password must throw WrongPasswordException when oldPassword is wrong")
    void updatePassword_MustThrowWrongPasswordException_WhenOldPasswordIsWrong() {
      BDDMockito.when(currentUserServiceMock.getCurrentUser()).thenReturn(guardianUser());
      BDDMockito.when(passwordEncoderMock.matches(any(String.class), any(String.class))).thenReturn(false);

      UpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequest.builder()
          .oldPassword("1111111111")
          .newPassword("12345678")
          .confirmPassword("12345678")
          .build();

      Assertions.assertThatExceptionOfType(WrongPasswordException.class)
          .isThrownBy(() -> userService.updatePassword(updatePasswordRequest))
          .withMessage("Old password is wrong");

      BDDMockito.verify(currentUserServiceMock).getCurrentUser();
      BDDMockito.verify(passwordEncoderMock).matches(any(String.class), any(String.class));
    }

  }

}