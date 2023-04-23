package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.request.UpdateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.GuardianNotFoundException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.mapper.GuardianMapper;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.User;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.service.impl.GuardianServiceImpl;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_GUARDIAN;
import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static br.com.emendes.adopetapi.util.GuardianUtils.*;
import static br.com.emendes.adopetapi.util.UserUtils.guardianUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for GuardianServiceImpl")
class GuardianServiceImplTest {

  @InjectMocks
  private GuardianServiceImpl guardianService;
  @Mock
  private GuardianMapper guardianMapperMock;
  @Mock
  private GuardianRepository guardianRepositoryMock;
  @Mock
  private PasswordEncoder passwordEncoderMock;
  @Mock
  private AuthenticationFacade authenticationFacadeMock;

  @Nested
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Captor
    private ArgumentCaptor<Guardian> guardianCaptor;

    @Test
    @DisplayName("Create must return GuardianResponse when create successfully")
    void create_MustReturnGuardianResponse_WhenCreateSuccessfully() {
      BDDMockito.when(guardianMapperMock.createGuardianRequestToGuardian(any(CreateGuardianRequest.class)))
          .thenReturn(guardianWithoutId());
      BDDMockito.when(guardianRepositoryMock.save(any(Guardian.class)))
          .thenReturn(guardian());
      BDDMockito.when(guardianMapperMock.guardianToGuardianResponse(any(Guardian.class)))
          .thenReturn(guardianResponse());
      BDDMockito.when(passwordEncoderMock.encode(any(String.class)))
          .thenReturn("1234567890");

      CreateGuardianRequest createGuardianRequest = CreateGuardianRequest.builder()
          .name("Lorem Ipsum")
          .email("lorem@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      GuardianResponse actualGuardianResponse = guardianService.create(createGuardianRequest);

      BDDMockito.verify(guardianMapperMock).createGuardianRequestToGuardian(any(CreateGuardianRequest.class));
      BDDMockito.verify(guardianMapperMock).guardianToGuardianResponse(any(Guardian.class));
      BDDMockito.verify(guardianRepositoryMock).save(guardianCaptor.capture());

      Guardian actualGuardian = guardianCaptor.getValue();

      Assertions.assertThat(actualGuardianResponse).isNotNull();
      Assertions.assertThat(actualGuardianResponse.id()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualGuardianResponse.name()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualGuardianResponse.email()).isNotNull().isEqualTo("lorem@email.com");
      Assertions.assertThat(actualGuardian).isNotNull();
      Assertions.assertThat(actualGuardian.getUser()).isNotNull();
      Assertions.assertThat(actualGuardian.getUser().getRoles()).isNotNull().contains(ROLE_GUARDIAN);
    }

    @Test
    @DisplayName("Create must throw PasswordsDoNotMatchException when passwords do not match")
    void create_MustThrowPasswordsDoNotMatchException_WhenPasswordsDoNotMatch() {
      CreateGuardianRequest createGuardianRequest = CreateGuardianRequest.builder()
          .password("123456789")
          .confirmPassword("12345678")
          .build();

      Assertions.assertThatExceptionOfType(PasswordsDoNotMatchException.class)
          .isThrownBy(() -> guardianService.create(createGuardianRequest))
          .withMessage("Passwords do not match");
    }

    @Test
    @DisplayName("Create must throw EmailAlreadyInUseException when already exists email in the database")
    void create_MustThrowEmailAlreadyInUseException_WhenAlreadyExistsEmailInTheDatabase() {
      BDDMockito.when(guardianMapperMock.createGuardianRequestToGuardian(any(CreateGuardianRequest.class)))
          .thenReturn(guardianWithoutId());
      BDDMockito.when(guardianRepositoryMock.save(any(Guardian.class)))
          .thenThrow(new DataIntegrityViolationException("unique_email constraint"));

      CreateGuardianRequest createGuardianRequest = CreateGuardianRequest.builder()
          .name("Lorem Ipsum")
          .email("lorem@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      Assertions.assertThatExceptionOfType(EmailAlreadyInUseException.class)
          .isThrownBy(() -> guardianService.create(createGuardianRequest))
          .withMessage("E-mail {lorem@email.com} is already in use");
    }

  }

  @Nested
  @DisplayName("Tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("Update must return GuardianResponse when update successfully")
    void update_MustReturnGuardianResponse_WhenUpdateSuccessfully() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser())
              .thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByIdAndUserAndDeletedFalse(eq(100L), any(User.class)))
          .thenReturn(Optional.of(guardian()));
      BDDMockito.when(guardianRepositoryMock.save(any(Guardian.class)))
          .thenReturn(updatedGuardian());
      BDDMockito.when(guardianMapperMock.guardianToGuardianResponse(any(Guardian.class)))
          .thenReturn(updatedGuardianResponse());

      UpdateGuardianRequest updateGuardianRequest = UpdateGuardianRequest.builder()
          .name("Lorem Ipsum Dolor")
          .email("loremdolor@email.com")
          .build();

      GuardianResponse actualGuardianResponse = guardianService.update(100L, updateGuardianRequest);

      BDDMockito.verify(guardianMapperMock).guardianToGuardianResponse(any(Guardian.class));
      BDDMockito.verify(guardianRepositoryMock).save(any(Guardian.class));
      BDDMockito.verify(guardianRepositoryMock).findByIdAndUserAndDeletedFalse(eq(100L), any(User.class));
      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();

      Assertions.assertThat(actualGuardianResponse).isNotNull();
      Assertions.assertThat(actualGuardianResponse.id()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualGuardianResponse.name()).isNotNull().isEqualTo("Lorem Ipsum Dolor");
      Assertions.assertThat(actualGuardianResponse.email()).isNotNull().isEqualTo("loremdolor@email.com");
    }

    @Test
    @DisplayName("Update must throw EmailAlreadyInUseException when already exists email in the database")
    void update_MustThrowEmailAlreadyInUseException_WhenAlreadyExistsEmailInTheDatabase() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser())
          .thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByIdAndUserAndDeletedFalse(eq(100L), any(User.class)))
          .thenReturn(Optional.of(guardian()));
      BDDMockito.when(guardianRepositoryMock.save(any(Guardian.class)))
          .thenThrow(new DataIntegrityViolationException("unique_email constraint"));

      UpdateGuardianRequest updateGuardianRequest = UpdateGuardianRequest.builder()
          .name("Lorem Ipsum Dolor")
          .email("loremdolor@email.com")
          .build();

      Assertions.assertThatExceptionOfType(EmailAlreadyInUseException.class)
          .isThrownBy(() -> guardianService.update(100L, updateGuardianRequest))
          .withMessage("E-mail {loremdolor@email.com} is already in use");

      BDDMockito.verify(guardianRepositoryMock).save(any(Guardian.class));
      BDDMockito.verify(guardianRepositoryMock).findByIdAndUserAndDeletedFalse(eq(100L), any(User.class));
      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
    }

    @Test
    @DisplayName("Update must throw GuardianNotFoundException when guardian not found with given id")
    void update_MustThrowGuardianNotFoundException_WhenGuardianNotFoundWithGivenId() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser())
          .thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByIdAndUserAndDeletedFalse(eq(100L), any(User.class)))
          .thenReturn(Optional.empty());

      UpdateGuardianRequest updateGuardianRequest = UpdateGuardianRequest.builder()
          .name("Lorem Ipsum Dolor")
          .email("loremdolor@email.com")
          .build();

      Assertions.assertThatExceptionOfType(GuardianNotFoundException.class)
          .isThrownBy(() -> guardianService.update(100L, updateGuardianRequest))
          .withMessage("Guardian not found");

      BDDMockito.verify(guardianRepositoryMock).findByIdAndUserAndDeletedFalse(eq(100L), any(User.class));
      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("FindById must return GuardianResponse when found successfully")
    void findById_MustReturnGuardianResponse_WhenFoundSuccessfully() {
      BDDMockito.when(guardianRepositoryMock.findByIdAndDeletedFalse(100L))
          .thenReturn(Optional.of(guardian()));
      BDDMockito.when(guardianMapperMock.guardianToGuardianResponse(any(Guardian.class)))
          .thenReturn(guardianResponse());

      GuardianResponse actualGuardianResponse = guardianService.findById(100L);

      BDDMockito.verify(guardianRepositoryMock).findByIdAndDeletedFalse(100L);
      BDDMockito.verify(guardianMapperMock).guardianToGuardianResponse(any(Guardian.class));

      Assertions.assertThat(actualGuardianResponse).isNotNull();
      Assertions.assertThat(actualGuardianResponse.id()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualGuardianResponse.name()).isNotNull().isEqualTo("Lorem Ipsum");
      Assertions.assertThat(actualGuardianResponse.email()).isNotNull().isEqualTo("lorem@email.com");
    }

    @Test
    @DisplayName("FindById must throw GuardianNotFoundException when guardian not found with given id")
    void findById_MustThrowGuardianNotFoundException_WhenGuardianNotFoundWithGivenId() {
      BDDMockito.when(guardianRepositoryMock.findByIdAndDeletedFalse(100L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(GuardianNotFoundException.class)
          .isThrownBy(() -> guardianService.findById(100L))
          .withMessage("Guardian not found");

      BDDMockito.verify(guardianRepositoryMock).findByIdAndDeletedFalse(100L);
    }

  }

  @Nested
  @DisplayName("Tests for fetchAll method")
  class FetchAllMethod {

    @Test
    @DisplayName("fetchAll must return Page<GuardianResponse> when fetch successfully")
    void fetchAll_MustReturnPageGuardianResponse_WhenFetchSuccessfully() {
      BDDMockito.when(guardianRepositoryMock.findByDeletedFalse(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(guardian()), PAGEABLE, 1));
      BDDMockito.when(guardianMapperMock.guardianToGuardianResponse(any(Guardian.class)))
          .thenReturn(guardianResponse());

      Page<GuardianResponse> actualGuardianResponsePage = guardianService.fetchAll(PAGEABLE);

      BDDMockito.verify(guardianRepositoryMock).findByDeletedFalse(any(Pageable.class));
      BDDMockito.verify(guardianMapperMock).guardianToGuardianResponse(any(Guardian.class));

      Assertions.assertThat(actualGuardianResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

  @Nested
  @DisplayName("Tests for deleteById method")
  class DeleteByIdMethod {

    @Test
    @DisplayName("DeleteById must save Guardian with enabled equals false when delete guardian")
    void deleteById_MustSaveGuardianWithEnabledEqualsFalse_WhenDeleteGuardian() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser())
              .thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByIdAndUserAndDeletedFalse(eq(100L), any(User.class)))
          .thenReturn(Optional.of(guardian()));
      BDDMockito.when(guardianRepositoryMock.save(any(Guardian.class)))
          .thenReturn(guardian());

      guardianService.deleteById(100L);

      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(guardianRepositoryMock).findByIdAndUserAndDeletedFalse(eq(100L), any(User.class));
      BDDMockito.verify(guardianRepositoryMock).save(any(Guardian.class));
    }

    @Test
    @DisplayName("DeleteById must throw GuardianNotFoundException when guardian do not exists with given id")
    void deleteById_MustThrowGuardianNotFoundException_WhenGuardianDoNotExistsWithGivenId() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser())
          .thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByIdAndUserAndDeletedFalse(eq(100L), any(User.class)))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(GuardianNotFoundException.class)
          .isThrownBy(() -> guardianService.deleteById(100L))
          .withMessage("Guardian not found");

      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(guardianRepositoryMock).findByIdAndUserAndDeletedFalse(eq(100L), any(User.class));
    }

  }

}