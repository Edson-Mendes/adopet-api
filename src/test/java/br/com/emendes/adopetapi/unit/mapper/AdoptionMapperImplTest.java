package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.config.ModelMapperConfig;
import br.com.emendes.adopetapi.dto.request.AdoptionRequest;
import br.com.emendes.adopetapi.mapper.impl.AdoptionMapperImpl;
import br.com.emendes.adopetapi.model.entity.Adoption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    Assertions.assertThat(actualAdoption.getPet()).isNotNull();
    Assertions.assertThat(actualAdoption.getPet().getId()).isNotNull().isEqualTo(10_000L);
    Assertions.assertThat(actualAdoption.getPet().getAge()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getName()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getCreatedAt()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getDescription()).isNull();
    Assertions.assertThat(actualAdoption.getPet().getShelter()).isNull();

    Assertions.assertThat(actualAdoption.getGuardian()).isNull();
  }

}