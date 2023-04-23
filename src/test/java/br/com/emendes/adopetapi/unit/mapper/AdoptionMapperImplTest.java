package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.config.ModelMapperConfig;
import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.dto.response.AdoptionResponse;
import br.com.emendes.adopetapi.mapper.impl.AdoptionMapperImpl;
import br.com.emendes.adopetapi.model.AdoptionStatus;
import br.com.emendes.adopetapi.model.entity.Adoption;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.Pet;
import br.com.emendes.adopetapi.util.ShelterUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.UserUtils.guardianUser;

@DisplayName("Unit tests for AdoptionMapperImpl")
class AdoptionMapperImplTest {

  private AdoptionMapperImpl adoptionMapper;

  @BeforeEach
  void setUp() {
    adoptionMapper = new AdoptionMapperImpl(new ModelMapperConfig().modelMapper());
  }

  @Test
  @DisplayName("AdoptionRequestToAdoption must return Adoption when map successfully")
  void adoptionRequestToAdoption_MustReturnAdoption_WhenMapSuccessfully() {
    AdoptionRequest adoptionRequest = AdoptionRequest.builder()
        .petId(10_000L)
        .build();

    Adoption actualAdoption = adoptionMapper.adoptionRequestToAdoption(adoptionRequest);

    Assertions.assertThat(actualAdoption).isNotNull();
    Assertions.assertThat(actualAdoption.getId()).isNull();
    Assertions.assertThat(actualAdoption.getDate()).isNull();

    Assertions.assertThat(actualAdoption.getPet()).isNotNull();
    Assertions.assertThat(actualAdoption.getPet().getId()).isNotNull().isEqualTo(10_000L);
    Assertions.assertThat(actualAdoption.getPet().getAge()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getName()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getCreatedAt()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getDescription()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getShelter()).isNull();

    Assertions.assertThat(actualAdoption.getGuardian()).isNull();
  }

  @Test
  @DisplayName("AdoptionToAdoptionResponse must return AdoptionResponse when map successfully")
  void adoptionToAdoptionResponse_MustReturnAdoptionResponse_WhenMapSuccessfully() {
    Pet pet = Pet.builder()
        .id(10_000L)
        .name("Dark")
        .description("A very calm and cute cat")
        .age("2 years old")
        .adopted(false)
        .createdAt(LocalDateTime.parse("2022-04-10T12:00:00"))
        .shelter(ShelterUtils.shelter())
        .build();

    Guardian guardian = Guardian.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .user(guardianUser())
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .build();

    Adoption adoption = Adoption.builder()
        .id(1_000_000L)
        .pet(pet)
        .guardian(guardian)
        .date(LocalDateTime.parse("2023-04-17T10:00:00"))
        .status(AdoptionStatus.ANALYSING)
        .build();

    AdoptionResponse actualAdoptionResponse = adoptionMapper.adoptionToAdoptionResponse(adoption);

    Assertions.assertThat(actualAdoptionResponse).isNotNull();
    Assertions.assertThat(actualAdoptionResponse.id()).isNotNull().isEqualTo(1_000_000L);
    Assertions.assertThat(actualAdoptionResponse.petId()).isNotNull().isEqualTo(10_000L);
    Assertions.assertThat(actualAdoptionResponse.guardianId()).isNotNull().isEqualTo(100L);
    Assertions.assertThat(actualAdoptionResponse.status()).isNotNull().isEqualTo(AdoptionStatus.ANALYSING);
    Assertions.assertThat(actualAdoptionResponse.date()).isNotNull().isEqualTo("2023-04-17T10:00:00");
  }

}