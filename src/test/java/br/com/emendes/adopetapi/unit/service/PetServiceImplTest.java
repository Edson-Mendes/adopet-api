package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.request.UpdatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
import br.com.emendes.adopetapi.exception.PetNotFoundException;
import br.com.emendes.adopetapi.mapper.PetMapper;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.repository.PetRepository;
import br.com.emendes.adopetapi.service.impl.PetServiceImpl;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static br.com.emendes.adopetapi.util.ConstantUtils.PAGEABLE;
import static br.com.emendes.adopetapi.util.PetUtils.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for PetServiceImpl")
class PetServiceImplTest {

  @InjectMocks
  private PetServiceImpl petService;
  @Mock
  private PetMapper petMapperMock;
  @Mock
  private PetRepository petRepositoryMock;

  @Nested
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("Create must return PetResponse when create successfully")
    void create_MustReturnPetResponse_WhenCreateSuccessfully() {
      BDDMockito.when(petMapperMock.createPetRequestToPet(any(CreatePetRequest.class)))
          .thenReturn(petWithoutId());
      BDDMockito.when(petRepositoryMock.save(any(Pet.class)))
          .thenReturn(pet());
      BDDMockito.when(petMapperMock.petToPetResponse(any(Pet.class)))
          .thenReturn(petResponse());

      CreatePetRequest createPetRequest = CreatePetRequest.builder()
          .name("Dark")
          .description("A very calm and cute cat")
          .age("2 years old")
          .shelterId(1_000L)
          .build();

      PetResponse actualPetResponse = petService.create(createPetRequest);

      BDDMockito.verify(petMapperMock).createPetRequestToPet(any(CreatePetRequest.class));
      BDDMockito.verify(petMapperMock).petToPetResponse(any(Pet.class));
      BDDMockito.verify(petRepositoryMock).save(any(Pet.class));

      Assertions.assertThat(actualPetResponse).isNotNull();
      Assertions.assertThat(actualPetResponse.getId()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualPetResponse.getName()).isNotNull().isEqualTo("Dark");
      Assertions.assertThat(actualPetResponse.getDescription()).isNotNull().isEqualTo("A very calm and cute cat");
      Assertions.assertThat(actualPetResponse.getAge()).isNotNull().isEqualTo("2 years old");
      Assertions.assertThat(actualPetResponse.getShelterId()).isNotNull().isEqualTo(1_000L);
      Assertions.assertThat(actualPetResponse.isAdopted()).isFalse();
    }

  }

  @Nested
  @DisplayName("Tests for fetchAll method")
  class FetchAllMethod {

    @Test
    @DisplayName("fetchAll must return Page<PetResponse> when fetch successfully")
    void fetchAll_MustReturnPagePetResponse_WhenFetchSuccessfully() {
      BDDMockito.when(petRepositoryMock.findByAdoptedFalse(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(pet()), PAGEABLE, 1));
      BDDMockito.when(petMapperMock.petToPetResponse(any(Pet.class)))
          .thenReturn(petResponse());

      Page<PetResponse> actualPetResponsePage = petService.fetchAll(PAGEABLE);

      BDDMockito.verify(petRepositoryMock).findByAdoptedFalse(any(Pageable.class));
      BDDMockito.verify(petMapperMock).petToPetResponse(any(Pet.class));

      Assertions.assertThat(actualPetResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

  @Nested
  @DisplayName("Tests for findById method")
  class FindByIdMethod {

    @Test
    @DisplayName("FindById must return PetResponse when found successfully")
    void findById_MustReturnPetResponse_WhenFoundSuccessfully() {
      BDDMockito.when(petRepositoryMock.findById(10_000L))
          .thenReturn(Optional.of(pet()));
      BDDMockito.when(petMapperMock.petToPetResponse(any(Pet.class)))
          .thenReturn(petResponse());

      PetResponse actualPetResponse = petService.findById(10_000L);

      BDDMockito.verify(petRepositoryMock).findById(10_000L);
      BDDMockito.verify(petMapperMock).petToPetResponse(any(Pet.class));

      Assertions.assertThat(actualPetResponse).isNotNull();
      Assertions.assertThat(actualPetResponse.getId()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualPetResponse.getName()).isNotNull().isEqualTo("Dark");
      Assertions.assertThat(actualPetResponse.getDescription()).isNotNull().isEqualTo("A very calm and cute cat");
      Assertions.assertThat(actualPetResponse.getAge()).isNotNull().isEqualTo("2 years old");
    }

    @Test
    @DisplayName("FindById must throw PetNotFoundException when pet not found with given id")
    void findById_MustThrowPetNotFoundException_WhenPetNotFoundWithGivenId() {
      BDDMockito.when(petRepositoryMock.findById(10_000L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(PetNotFoundException.class)
          .isThrownBy(() -> petService.findById(10_000L))
          .withMessage("Pet not found");
    }

  }

  @Nested
  @DisplayName("Tests for update method")
  class UpdateMethod {

    @Test
    @DisplayName("Update must return PetResponse when update successfully")
    void update_MustReturnPetResponse_WhenUpdateSuccessfully() {
      BDDMockito.when(petRepositoryMock.findById(10_000L))
          .thenReturn(Optional.of(pet()));
      BDDMockito.when(petRepositoryMock.save(any(Pet.class)))
          .thenReturn(updatedPet());
      BDDMockito.when(petMapperMock.petToPetResponse(any(Pet.class)))
          .thenReturn(updatedPetResponse());

      UpdatePetRequest petRequest = UpdatePetRequest.builder()
          .name("Darkness")
          .description("A very cute cat")
          .age("3 years old")
          .build();

      PetResponse actualPetResponse = petService.update(10_000L, petRequest);

      BDDMockito.verify(petMapperMock).petToPetResponse(any(Pet.class));
      BDDMockito.verify(petRepositoryMock).save(any(Pet.class));
      BDDMockito.verify(petRepositoryMock).findById(10_000L);

      Assertions.assertThat(actualPetResponse).isNotNull();
      Assertions.assertThat(actualPetResponse.getId()).isNotNull().isEqualTo(10_000L);
      Assertions.assertThat(actualPetResponse.getName()).isNotNull().isEqualTo("Darkness");
      Assertions.assertThat(actualPetResponse.getDescription()).isNotNull().isEqualTo("A very cute cat");
      Assertions.assertThat(actualPetResponse.getAge()).isNotNull().isEqualTo("3 years old");
    }

    @Test
    @DisplayName("Update must throw PetNotFoundException when pet not found with given id")
    void update_MustThrowPetNotFoundException_WhenPetNotFoundWithGivenId() {
      BDDMockito.when(petRepositoryMock.findById(10_000L))
          .thenReturn(Optional.empty());

      UpdatePetRequest petRequest = UpdatePetRequest.builder()
          .name("Darkness")
          .description("A very cute cat")
          .age("3 years old")
          .build();

      Assertions.assertThatExceptionOfType(PetNotFoundException.class)
          .isThrownBy(() -> petService.update(1000L, petRequest))
          .withMessage("Pet not found");
    }

  }

  @Nested
  @DisplayName("Tests for deleteById method")
  class DeleteByIdMethod {

    @Test
    @DisplayName("DeleteById must call PetRepository#delete when delete pet")
    void deleteById_MustCallPetRepositoryDelete_WhenDeletePet() {
      BDDMockito.when(petRepositoryMock.findById(10_000L))
          .thenReturn(Optional.of(pet()));
      BDDMockito.doNothing().when(petRepositoryMock).delete(any(Pet.class));

      petService.deleteById(10_000L);

      BDDMockito.verify(petRepositoryMock).findById(10_000L);
      BDDMockito.verify(petRepositoryMock).delete(any(Pet.class));
    }

    @Test
    @DisplayName("DeleteById must throw PetNotFoundException when pet do not exists with given id")
    void deleteById_MustThrowPetNotFoundException_WhenPetDoNotExistsWithGivenId() {
      BDDMockito.when(petRepositoryMock.findById(10_000L))
          .thenReturn(Optional.empty());

      Assertions.assertThatExceptionOfType(PetNotFoundException.class)
          .isThrownBy(() -> petService.deleteById(10_000L))
          .withMessage("Pet not found");
    }

  }

}