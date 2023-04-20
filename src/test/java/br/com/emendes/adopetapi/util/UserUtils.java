package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.model.entity.User;

import java.util.List;

import static br.com.emendes.adopetapi.util.RoleUtils.guardianRole;
import static br.com.emendes.adopetapi.util.RoleUtils.shelterRole;

public abstract class UserUtils {

  public static User guardianUser() {
    return User.builder()
        .id(10L)
        .email("lorem@email.com")
        .password("1234567890")
        .enabled(true)
        .roles(List.of(guardianRole()))
        .build();
  }

  public static User guardianUserWithoutId() {
    return User.builder()
        .id(null)
        .email("lorem@email.com")
        .password("1234567890")
        .enabled(true)
        .build();
  }

  public static User shelterUser() {
    return User.builder()
        .id(11L)
        .email("animal.shelter@email.com")
        .password("1234567890")
        .enabled(true)
        .roles(List.of(shelterRole()))
        .build();
  }

  public static User shelterUserWithoutId() {
    return User.builder()
        .id(null)
        .email("animal.shelter@email.com")
        .password("1234567890")
        .enabled(true)
        .build();
  }

}
