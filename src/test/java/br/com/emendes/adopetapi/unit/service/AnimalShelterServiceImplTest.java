package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.AnimalShelterRequest;
import br.com.emendes.adopetapi.dto.response.AnimalShelterResponse;
import br.com.emendes.adopetapi.mapper.AnimalShelterMapper;
import br.com.emendes.adopetapi.model.entity.AnimalShelter;
import br.com.emendes.adopetapi.repository.AnimalShelterRepository;
import br.com.emendes.adopetapi.service.impl.AnimalShelterServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.emendes.adopetapi.util.ShelterUtils.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@DisplayName("Unit tests for AnimalShelterServiceImpl")
class AnimalShelterServiceImplTest {

  @InjectMocks
  private AnimalShelterServiceImpl animalShelterService;
  @Mock
  private AnimalShelterMapper animalShelterMapperMock;
  @Mock
  private AnimalShelterRepository animalShelterRepositoryMock;

  @Nested
  @DisplayName("Tests for create method")
  class CreateMethod {

    @Test
    @DisplayName("Create must return AnimalShelterResponse when create successfully")
    void create_MustReturnAnimalShelterResponse_WhenCreateSuccessfully() {
      BDDMockito.when(animalShelterMapperMock.animalShelterRequestToAnimalShelter(any(AnimalShelterRequest.class)))
          .thenReturn(animalShelterWithoutId());
      BDDMockito.when(animalShelterRepositoryMock.save(any(AnimalShelter.class)))
          .thenReturn(animalShelter());
      BDDMockito.when(animalShelterMapperMock.animalShelterToAnimalShelterResponse(any(AnimalShelter.class)))
          .thenReturn(animalShelterResponse());

      AnimalShelterRequest shelterRequest = AnimalShelterRequest.builder()
          .name("Animal Shelter")
          .build();

      AnimalShelterResponse actualAnimalShelterResponse = animalShelterService.create(shelterRequest);

      BDDMockito.verify(animalShelterMapperMock).animalShelterRequestToAnimalShelter(any(AnimalShelterRequest.class));
      BDDMockito.verify(animalShelterMapperMock).animalShelterToAnimalShelterResponse(any(AnimalShelter.class));
      BDDMockito.verify(animalShelterRepositoryMock).save(any(AnimalShelter.class));

      Assertions.assertThat(actualAnimalShelterResponse).isNotNull();
      Assertions.assertThat(actualAnimalShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualAnimalShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
    }

  }

}