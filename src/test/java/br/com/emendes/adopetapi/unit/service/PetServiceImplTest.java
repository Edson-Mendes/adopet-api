package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.CreatePetRequest;
import br.com.emendes.adopetapi.dto.response.PetResponse;
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
      BDDMockito.when(petRepositoryMock.findAll(PAGEABLE))
          .thenReturn(new PageImpl<>(List.of(pet()), PAGEABLE, 1));
      BDDMockito.when(petMapperMock.petToPetResponse(any(Pet.class)))
          .thenReturn(petResponse());

      Page<PetResponse> actualPetResponsePage = petService.fetchAll(PAGEABLE);

      BDDMockito.verify(petRepositoryMock).findAll(any(Pageable.class));
      BDDMockito.verify(petMapperMock).petToPetResponse(any(Pet.class));

      Assertions.assertThat(actualPetResponsePage).isNotNull().isNotEmpty().hasSize(1);
    }

  }

}