package br.com.emendes.adopetapi.unit.service;

import br.com.emendes.adopetapi.dto.request.ShelterRequest;
import br.com.emendes.adopetapi.dto.response.ShelterResponse;
import br.com.emendes.adopetapi.mapper.ShelterMapper;
import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.repository.ShelterRepository;
import br.com.emendes.adopetapi.service.impl.ShelterServiceImpl;
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

    @Test
    @DisplayName("Create must return ShelterResponse when create successfully")
    void create_MustReturnShelterResponse_WhenCreateSuccessfully() {
      BDDMockito.when(shelterMapperMock.shelterRequestToShelter(any(ShelterRequest.class)))
          .thenReturn(shelterWithoutId());
      BDDMockito.when(shelterRepositoryMock.save(any(Shelter.class)))
          .thenReturn(shelter());
      BDDMockito.when(shelterMapperMock.shelterToShelterResponse(any(Shelter.class)))
          .thenReturn(shelterResponse());

      ShelterRequest shelterRequest = ShelterRequest.builder()
          .name("Animal Shelter")
          .build();

      ShelterResponse actualShelterResponse = shelterService.create(shelterRequest);

      BDDMockito.verify(shelterMapperMock).shelterRequestToShelter(any(ShelterRequest.class));
      BDDMockito.verify(shelterMapperMock).shelterToShelterResponse(any(Shelter.class));
      BDDMockito.verify(shelterRepositoryMock).save(any(Shelter.class));

      Assertions.assertThat(actualShelterResponse).isNotNull();
      Assertions.assertThat(actualShelterResponse.getId()).isNotNull().isEqualTo(1000L);
      Assertions.assertThat(actualShelterResponse.getName()).isNotNull().isEqualTo("Animal Shelter");
    }

  }

}