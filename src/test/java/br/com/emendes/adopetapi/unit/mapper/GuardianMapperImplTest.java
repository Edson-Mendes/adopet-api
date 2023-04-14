package br.com.emendes.adopetapi.unit.mapper;

import br.com.emendes.adopetapi.config.ModelMapperConfig;
import br.com.emendes.adopetapi.dto.request.CreateGuardianRequest;
import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.mapper.impl.GuardianMapperImpl;
import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Unit tests for GuardianMapperImpl")
class GuardianMapperImplTest {

  private GuardianMapperImpl guardianMapper;

  @BeforeEach
  void setUp() {
    guardianMapper = new GuardianMapperImpl(new ModelMapperConfig().modelMapper());
  }

  @Test
  @DisplayName("CreateGuardianRequestToGuardian must return Guardian when map successfully")
  void guardianRequestToGuardian_MustReturnGuardian_WhenMapSuccessfully() {
    CreateGuardianRequest createGuardianRequest = CreateGuardianRequest.builder()
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .confirmPassword("1234567890")
        .build();

    Guardian actualGuardian = guardianMapper.createGuardianRequestToGuardian(createGuardianRequest);

    Assertions.assertThat(actualGuardian).isNotNull();
    Assertions.assertThat(actualGuardian.getId()).isNull();
    Assertions.assertThat(actualGuardian.getCreatedAt()).isNull();
    Assertions.assertThat(actualGuardian.getName()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualGuardian.getUser()).isNotNull();
    Assertions.assertThat(actualGuardian.getUser().getId()).isNull();
    Assertions.assertThat(actualGuardian.getUser().getEmail()).isNotNull().isEqualTo("lorem@email.com");
    Assertions.assertThat(actualGuardian.getUser().getPassword()).isNotNull().isEqualTo("1234567890");
  }

  @Test
  @DisplayName("GuardianToGuardianResponse must return GuardianResponse when map successfully")
  void guardianToGuardianResponse_MustReturnGuardianResponse_WhenMapSuccessfully() {
    User user = User.builder()
        .id(10L)
        .email("lorem@email.com")
        .password("1234567890")
        .build();
    Guardian guardian = Guardian.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .user(user)
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .build();

    GuardianResponse actualGuardianResponse = guardianMapper.guardianToGuardianResponse(guardian);

    Assertions.assertThat(actualGuardianResponse).isNotNull();
    Assertions.assertThat(actualGuardianResponse.getId()).isNotNull().isEqualTo(100L);
    Assertions.assertThat(actualGuardianResponse.getName()).isNotNull().isEqualTo("Lorem Ipsum");
    Assertions.assertThat(actualGuardianResponse.getEmail()).isNotNull().isEqualTo("lorem@email.com");
  }

}