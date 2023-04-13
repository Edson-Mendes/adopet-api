package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.GuardianResponse;
import br.com.emendes.adopetapi.model.entity.Guardian;

import java.time.LocalDateTime;

import static br.com.emendes.adopetapi.util.UserUtils.guardianUser;
import static br.com.emendes.adopetapi.util.UserUtils.guardianUserWithoutId;

public abstract class GuardianUtils {

  public static GuardianResponse guardianResponse() {
    return GuardianResponse.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .build();
  }

  public static GuardianResponse updatedGuardianResponse() {
    return GuardianResponse.builder()
        .id(100L)
        .name("Lorem Ipsum Dolor")
        .email("loremdolor@email.com")
        .build();
  }

  public static Guardian guardian() {

    return Guardian.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .user(guardianUser())
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .build();
  }

  public static Guardian updatedGuardian() {
    return Guardian.builder()
        .id(100L)
        .name("Lorem Ipsum Dolor")
        .user(guardianUser())
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .build();
  }

  public static Guardian guardianWithoutId() {
    return Guardian.builder()
        .id(null)
        .name("Lorem Ipsum")
        .user(guardianUserWithoutId())
        .createdAt(null)
        .build();
  }

}
