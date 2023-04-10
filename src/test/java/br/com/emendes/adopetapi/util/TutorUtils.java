package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.dto.response.TutorResponse;
import br.com.emendes.adopetapi.model.entity.Tutor;

import java.time.LocalDateTime;

public abstract class TutorUtils {

  public static TutorResponse tutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .build();
  }

  public static TutorResponse updatedTutorResponse() {
    return TutorResponse.builder()
        .id(100L)
        .name("Lorem Ipsum Dolor")
        .email("loremdolor@email.com")
        .build();
  }

  public static Tutor tutor() {
    return Tutor.builder()
        .id(100L)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .build();
  }

  public static Tutor updatedTutor() {
    return Tutor.builder()
        .id(100L)
        .name("Lorem Ipsum Dolor")
        .email("loremdolor@email.com")
        .password("1234567890")
        .createdAt(LocalDateTime.parse("2023-04-02T10:30:00"))
        .build();
  }

  public static Tutor tutorWithoutId() {
    return Tutor.builder()
        .id(null)
        .name("Lorem Ipsum")
        .email("lorem@email.com")
        .password("1234567890")
        .createdAt(null)
        .build();
  }

}
