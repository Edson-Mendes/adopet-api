package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.exception.GuardianNotFoundException;
import br.com.emendes.adopetapi.exception.InvalidArgumentException;
import br.com.emendes.adopetapi.exception.ShelterNotFoundException;
import br.com.emendes.adopetapi.exception.UserIsNotAuthenticateException;
import br.com.emendes.adopetapi.mapper.AdoptionMapper;
import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.AdoptionRepository;
import br.com.emendes.adopetapi.repository.GuardianRepository;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.impl.AdoptionServiceImpl;
import br.com.emendes.adopetapi.util.AuthenticationFacade;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static br.com.emendes.adopetapi.util.AdoptionUtils.*;
import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static br.com.emendes.adopetapi.util.GuardianUtils.guardian;
import static br.com.emendes.adopetapi.util.ShelterUtils.shelter;
import static br.com.emendes.adopetapi.util.UserUtils.guardianUser;
import static br.com.emendes.adopetapi.util.UserUtils.shelterUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for AdoptionServiceImpl")
class AdoptionServiceImplTest {

  @InjectMocks
  private AdoptionServiceImpl adoptionService;
  @Mock
  private AdoptionMapper adoptionMapperMock;
  @Mock
  private AuthenticationFacade authenticationFacadeMock;
  @Mock
  private GuardianRepository guardianRepositoryMock;
  @Mock
  private ShelterRepository shelterRepositoryMock;
  @Mock
  private PetRepository petRepositoryMock;
  @Mock
  private AdoptionRepository adoptionRepositoryMock;

  @Nested
  @DisplayName("Tests for adopt method")
  class AdoptMethod {

    @Test
    @DisplayName("Adopt must return AdoptionResponse when adopt successfully")
    void adopt_MustReturnAdoptionResponse_WhenAdoptSuccessfully() {
      BDDMockito.when(petRepositoryMock.existsById(10_000L)).thenReturn(true);
      BDDMockito.when(authenticationFacadeMock.getCurrentUser()).thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByUserId(10L)).thenReturn(Optional.of(guardian()));
      BDDMockito.when(adoptionMapperMock.adoptionRequestToAdoption(any(AdoptionRequest.class)))
          .thenReturn(adoptionWithoutId());
      BDDMockito.when(adoptionRepositoryMock.save(any(Adoption.class))).thenReturn(adoption());
      BDDMockito.when(adoptionMapperMock.adoptionToAdoptionResponse(any(Adoption.class))).thenReturn(adoptionResponse());

      AdoptionRequest adoptionRequest = AdoptionRequest.builder()
          .petId(10_000L)
          .build();

      AdoptionResponse actualAdoptionResponse = adoptionService.adopt(adoptionRequest);

      BDDMockito.verify(petRepositoryMock).existsById(10_000L);
      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(guardianRepositoryMock).findByUserId(10L);
      BDDMockito.verify(adoptionMapperMock).adoptionRequestToAdoption(any());
      BDDMockito.verify(adoptionRepositoryMock).save(any());
      BDDMockito.verify(adoptionMapperMock).adoptionToAdoptionResponse(any());

      Assertions.assertThat(actualAdoptionResponse).isNotNull();
      Assertions.assertThat(actualAdoptionResponse.getId()).isNotNull().isEqualTo(1_000_000L);
      Assertions.assertThat(actualAdoptionResponse.getPetId()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualAdoptionResponse.getGuardianId()).isNotNull().isEqualTo(100L);
      Assertions.assertThat(actualAdoptionResponse.getStatus()).isNotNull().isEqualTo(AdoptionStatus.ANALYSING);
    }

    @Test
    @DisplayName("Adopt must throw InvalidArgumentException when do not exists pet with id 10_000L")
    void adopt_MustThrowInvalidArgumentException_WhenDoNotExistsPetWithId10_000L() {
      AdoptionRequest adoptionRequest = AdoptionRequest.builder()
          .petId(10_000L)
          .build();

      Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
          .isThrownBy(() -> adoptionService.adopt(adoptionRequest))
          .withMessage("Invalid pet id");

      BDDMockito.verify(petRepositoryMock).existsById(10_000L);
    }

    @Test
    @DisplayName("Adopt must throw UserIsNotAuthenticateException when User is not authenticate")
    void adopt_MustThrowUserNotAuthenticateException_WhenUserIsNotAuthenticate() {
      BDDMockito.when(petRepositoryMock.existsById(10_000L)).thenReturn(true);
      BDDMockito.when(authenticationFacadeMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticateException("User is not authenticate"));

      AdoptionRequest adoptionRequest = AdoptionRequest.builder()
          .petId(10_000L)
          .build();

      Assertions.assertThatExceptionOfType(UserIsNotAuthenticateException.class)
          .isThrownBy(() -> adoptionService.adopt(adoptionRequest))
          .withMessage("User is not authenticate");

      BDDMockito.verify(petRepositoryMock).existsById(10_000L);
      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
    }

    @Test
    @DisplayName("Adopt must throw InvalidArgumentException when not found current user")
    void adopt_MustThrowInvalidArgumentException_WhenNotFoundCurrentUser() {
      BDDMockito.when(petRepositoryMock.existsById(10_000L)).thenReturn(true);
      BDDMockito.when(authenticationFacadeMock.getCurrentUser()).thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByUserId(any()))
          .thenThrow(new InvalidArgumentException("Current user not found"));

      AdoptionRequest adoptionRequest = AdoptionRequest.builder()
          .petId(10_000L)
          .build();

      Assertions.assertThatExceptionOfType(InvalidArgumentException.class)
          .isThrownBy(() -> adoptionService.adopt(adoptionRequest))
          .withMessage("Current user not found");

      BDDMockito.verify(petRepositoryMock).existsById(10_000L);
      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(guardianRepositoryMock).findByUserId(any());
    }

  }

  @Nested
  @DisplayName("Tests for fetchAll method")
  class FetchAllMethod {

    @Test
    @DisplayName("FetchAll must return Page<AdoptionResponse> when fetch all for guardian successfully")
    void fetchAll_MustReturnPageAdoptionResponse_WhenFetchAllForGuardianSuccessfully() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser()).thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByUserId(10L)).thenReturn(Optional.of(guardian()));
      BDDMockito.when(adoptionRepositoryMock.findAllByGuardian(any(Guardian.class), eq(PAGEABLE)))
          .thenReturn(new PageImpl<>(List.of(adoption()), PAGEABLE, 1));
      BDDMockito.when(adoptionMapperMock.adoptionToAdoptionResponse(any(Adoption.class)))
          .thenReturn(adoptionResponse());

      Page<AdoptionResponse> actualAdoptionResponsePage = adoptionService.fetchAll(PAGEABLE);

      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(guardianRepositoryMock).findByUserId(any());
      BDDMockito.verify(adoptionRepositoryMock).findAllByGuardian(any(), any());
      BDDMockito.verify(adoptionMapperMock).adoptionToAdoptionResponse(any());

      Assertions.assertThat(actualAdoptionResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("FetchAll must return Page<AdoptionResponse> when fetch all for shelter successfully")
    void fetchAll_MustReturnPageAdoptionResponse_WhenFetchAllForShelterSuccessfully() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser()).thenReturn(shelterUser());
      BDDMockito.when(shelterRepositoryMock.findByUserId(11L)).thenReturn(Optional.of(shelter()));
      BDDMockito.when(adoptionRepositoryMock.findAllByPetShelter(any(Shelter.class), eq(PAGEABLE)))
          .thenReturn(new PageImpl<>(List.of(adoption()), PAGEABLE, 1));
      BDDMockito.when(adoptionMapperMock.adoptionToAdoptionResponse(any(Adoption.class)))
          .thenReturn(adoptionResponse());

      Page<AdoptionResponse> actualAdoptionResponsePage = adoptionService.fetchAll(PAGEABLE);

      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(shelterRepositoryMock).findByUserId(any());
      BDDMockito.verify(adoptionRepositoryMock).findAllByPetShelter(any(), any());
      BDDMockito.verify(adoptionMapperMock).adoptionToAdoptionResponse(any());

      Assertions.assertThat(actualAdoptionResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("FetchAll must throw UserIsNotAuthenticateException when user is not authenticate")
    void fetchAll_MustThrowUserIsNotAuthenticateException_WhenUserIsNotAuthenticate() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser())
          .thenThrow(new UserIsNotAuthenticateException("User is not authenticate"));


      Assertions.assertThatExceptionOfType(UserIsNotAuthenticateException.class)
          .isThrownBy(() -> adoptionService.fetchAll(PAGEABLE))
          .withMessage("User is not authenticate");

      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
    }

    @Test
    @DisplayName("FetchAll must throw ShelterNotFoundException when Shelter not found")
    void fetchAll_MustThrowShelterNotFoundException_WhenShelterNotFound() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser()).thenReturn(shelterUser());
      BDDMockito.when(shelterRepositoryMock.findByUserId(11L))
          .thenReturn(Optional.empty());


      Assertions.assertThatExceptionOfType(ShelterNotFoundException.class)
          .isThrownBy(() -> adoptionService.fetchAll(PAGEABLE))
          .withMessage("Shelter not found");

      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(shelterRepositoryMock).findByUserId(any());
    }

    @Test
    @DisplayName("FetchAll must throw GuardianNotFoundException when Guardian not found")
    void fetchAll_MustThrowGuardianNotFoundException_WhenGuardianNotFound() {
      BDDMockito.when(authenticationFacadeMock.getCurrentUser()).thenReturn(guardianUser());
      BDDMockito.when(guardianRepositoryMock.findByUserId(11L))
          .thenReturn(Optional.empty());


      Assertions.assertThatExceptionOfType(GuardianNotFoundException.class)
          .isThrownBy(() -> adoptionService.fetchAll(PAGEABLE))
          .withMessage("Guardian not found");

      BDDMockito.verify(authenticationFacadeMock).getCurrentUser();
      BDDMockito.verify(guardianRepositoryMock).findByUserId(any());
    }

  }

}