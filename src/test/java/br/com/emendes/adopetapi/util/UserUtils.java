package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.model.entity.User;

public abstract class UserUtils {

  public static User guardianUser() {
    return User.builder()
        .id(10L)
        .email("lorem@email.com")
        .password("1234567890")
        .build();
  }

  public static User guardianUserWithoutId() {
    return User.builder()
        .id(null)
        .email("lorem@email.com")
        .password("1234567890")
        .build();
  }

}
