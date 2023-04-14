package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.CreateShelterRequest;
import br.com.emendes.adopetapi.dto.request.UpdateShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.exception.EmailAlreadyInUseException;
import br.com.emendes.adopetapi.exception.PasswordsDoNotMatchException;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.impl.ShelterServiceImpl;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static br.com.emendes.adopetapi.util.ConstantUtil.ROLE_SHELTER;
import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static br.com.emendes.adopetapi.util.ShelterUtils.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for ShelterServiceImpl")
class ShelterServiceImplTest {

  @InjectMocks
  private ShelterServiceImpl shelterService;
  @Mock
  private ShelterMapper shelterMapperMock;
  @Mock
  private ShelterRepository shelterRepositoryMock;

  @Nested
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Captor
    private ArgumentCaptor<Shelter> shelterCaptor;

    @Test
    @DisplayName("Create must return ShelterResponse when create successfully")
    void create_MustReturnShelterResponse_WhenCreateSuccessfully() {
      BDDMockito.when(shelterMapperMock.createShelterRequestToShelter(any(CreateShelterRequest.class)))
          .thenReturn(shelterWithoutId());
      BDDMockito.when(shelterRepositoryMock.save(any(Shelter.class)))
          .thenReturn(shelter());
      BDDMockito.when(shelterMapperMock.shelterToShelterResponse(any(Shelter.class)))
          .thenReturn(shelterResponse());

      CreateShelterRequest createShelterRequest = CreateShelterRequest.builder()
          .name("Animal Shelter")
          .email("animal.shelter@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      ShelterResponse actualShelterResponse = shelterService.create(createShelterRequest);

      BDDMockito.verify(shelterMapperMock).createShelterRequestToShelter(any(CreateShelterRequest.class));
      BDDMockito.verify(shelterMapperMock).shelterToShelterResponse(any(Shelter.class));
      BDDMockito.verify(shelterRepositoryMock).save(shelterCaptor.capture());

      Shelter actualShelter = shelterCaptor.getValue();

      Assertions.assertThat(actualShelterResponse).isNotNull();
      Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1_000L);
      Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
      Assertions.assertThat(actualShelterResponse.getEmail()).isNotNull().isEqualTo("animal.shelter@email.com");
      Assertions.assertThat(actualShelter).isNotNull();
      Assertions.assertThat(actualShelter.getUser()).isNotNull();
      Assertions.assertThat(actualShelter.getUser().getRoles()).isNotNull().contains(ROLE_SHELTER);
    }

    @Test
    @DisplayName("Create must throw PasswordsDoNotMatchException when passwords do not match")
    void create_MustThrowPasswordsDoNotMatchException_WhenPasswordsDoNotMatch() {
      CreateShelterRequest createShelterRequest = CreateShelterRequest.builder()
          .password("123456789")
          .confirmPassword("12345678")
          .build();

      Assertions.assertThatExceptionOfType(PasswordsDoNotMatchException.class)
          .isThrownBy(() -> shelterService.create(createShelterRequest))
          .withMessage("Passwords do not match");
    }

    @Test
    @DisplayName("Create must throw EmailAlreadyInUseException when already exists email in the database")
    void create_MustThrowEmailAlreadyInUseException_WhenAlreadyExistsEmailInTheDatabase() {
      BDDMockito.when(shelterMapperMock.createShelterRequestToShelter(any(CreateShelterRequest.class)))
          .thenReturn(shelterWithoutId());
      BDDMockito.when(shelterRepositoryMock.save(any(Shelter.class)))
          .thenThrow(new DataIntegrityViolationException("unique_email constraint"));

      CreateShelterRequest createShelterRequest = CreateShelterRequest.builder()
          .name("Animal Shelter")
          .email("animal.shelter@email.com")
          .password("1234567890")
          .confirmPassword("1234567890")
          .build();

      Assertions.assertThatExceptionOfType(EmailAlreadyInUseException.class)
          .isThrownBy(() -> shelterService.create(createShelterRequest))
          .withMessage("E-mail {animal.shelter@email.com} is already in use");
    }

  }

  @Nested
  @DisplayName("Tests for fetchAll method")
  class FetchAllMethod {

    @Test
    @DisplayName("fetchAll must return Page<ShelterResponse> when fetch successfully")
    void fetchAll_MustReturnPageShelterResponse_WhenFetchSuccessfully() {
      BDDMockito.when(shelterRepositoryMock.findAll(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(shelter()), PAGEABLE, 1));
      BDDMockito.when(shelterMapperMock.shelterToShelterResponse(any(Shelter.class)))
          .thenReturn(shelterResponse());

      Page<ShelterResponse> actualShelterResponsePage = shelterService.fetchAll(PAGEABLE);

      BDDMockito.verify(shelterRepositoryMock).findAll(any(Pageable.class));
      BDDMockito.verify(shelterMapperMock).shelterToShelterResponse(any(Shelter.class));

      Assertions.assertThat(actualShelterResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("FindById must return ShelterResponse when found successfully")
    void findById_MustReturnShelterResponse_WhenFoundSuccessfully() {
      BDDMockito.when(shelterRepositoryMock.findById(1000L))
          .thenReturn(Optional.of(shelter()));
      BDDMockito.when(shelterMapperMock.shelterToShelterResponse(any(Shelter.class)))
          .thenReturn(shelterResponse());

      ShelterResponse actualShelterResponse = shelterService.findById(1000L);

      BDDMockito.verify(shelterRepositoryMock).findById(1000L);
      BDDMockito.verify(shelterMapperMock).shelterToShelterResponse(any(Shelter.class));

      Assertions.assertThat(actualShelterResponse).isNotNull();
      Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
    }

    @Test
    @DisplayName("FindById must throw ShelterNotFoundException when shelter not found with given id")
    void findById_MustThrowShelterNotFoundException_WhenShelterNotFoundWithGivenId() {
      BDDMockito.when(shelterRepositoryMock.findById(1000L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(ShelterNotFoundException.class)
          .isThrownBy(() -> shelterService.findById(1000L))
          .withMessage("Shelter not found");
    }

  }

  @Nested
  @DisplayName("Tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("Update must return ShelterResponse when update successfully")
    void update_MustReturnShelterResponse_WhenUpdateSuccessfully() {
      BDDMockito.when(shelterRepositoryMock.findById(1000L))
          .thenReturn(Optional.of(shelter()));
      BDDMockito.when(shelterRepositoryMock.save(any(Shelter.class)))
          .thenReturn(updatedShelter());
      BDDMockito.when(shelterMapperMock.shelterToShelterResponse(any(Shelter.class)))
          .thenReturn(updatedShelterResponse());

      UpdateShelterRequest updateShelterRequest = UpdateShelterRequest.builder()
          .name("Animal Shelter ABC")
          .build();

      ShelterResponse actualShelterResponse = shelterService.update(1000L, updateShelterRequest);

      BDDMockito.verify(shelterMapperMock).shelterToShelterResponse(any(Shelter.class));
      BDDMockito.verify(shelterRepositoryMock).save(any(Shelter.class));
      BDDMockito.verify(shelterRepositoryMock).findById(1000L);

      Assertions.assertThat(actualShelterResponse).isNotNull();
      Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter ABC");
    }

    @Test
    @DisplayName("Update must throw EmailAlreadyInUseException when already exists email in the database")
    void update_MustThrowEmailAlreadyInUseException_WhenAlreadyExistsEmailInTheDatabase() {
      BDDMockito.when(shelterRepositoryMock.findById(100L))
          .thenReturn(Optional.of(shelter()));
      BDDMockito.when(shelterRepositoryMock.save(any(Shelter.class)))
          .thenThrow(new DataIntegrityViolationException("unique_email constraint"));

      UpdateShelterRequest updateShelterRequest = UpdateShelterRequest.builder()
          .name("Lorem Ipsum Dolor")
          .email("shelter@email.com")
          .build();

      Assertions.assertThatExceptionOfType(EmailAlreadyInUseException.class)
          .isThrownBy(() -> shelterService.update(100L, updateShelterRequest))
          .withMessage("E-mail {shelter@email.com} is already in use");
    }

    @Test
    @DisplayName("Update must throw ShelterNotFoundException when shelter not found with given id")
    void update_MustThrowShelterNotFoundException_WhenShelterNotFoundWithGivenId() {
      BDDMockito.when(shelterRepositoryMock.findById(1000L))
          .thenReturn(Optional.empty());

      UpdateShelterRequest updateShelterRequest = UpdateShelterRequest.builder()
          .name("Animal Shelter ABC")
          .build();

      Assertions.assertThatExceptionOfType(ShelterNotFoundException.class)
          .isThrownBy(() -> shelterService.update(1000L, updateShelterRequest))
          .withMessage("Shelter not found");
    }

  }

  @Nested
  @DisplayName("Tests for deleteById method")
  class DeleteByIdMethod {

    @Test
    @DisplayName("DeleteById must call ShelterRepository#delete when delete shelter")
    void deleteById_MustCallShelterRepositoryDelete_WhenDeleteShelter() {
      BDDMockito.when(shelterRepositoryMock.findById(1_000L))
          .thenReturn(Optional.of(shelter()));
      BDDMockito.doNothing().when(shelterRepositoryMock).delete(any(Shelter.class));

      shelterService.deleteById(1_000L);

      BDDMockito.verify(shelterRepositoryMock).findById(1_000L);
      BDDMockito.verify(shelterRepositoryMock).delete(any(Shelter.class));
    }

    @Test
    @DisplayName("DeleteById must throw ShelterNotFoundException when shelter do not exists with given id")
    void deleteById_MustThrowShelterNotFoundException_WhenShelterDoNotExistsWithGivenId() {
      BDDMockito.when(shelterRepositoryMock.findById(1_000L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(ShelterNotFoundException.class)
          .isThrownBy(() -> shelterService.deleteById(1_000L))
          .withMessage("Shelter not found");
    }

  }

}