package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.model.entity.Role;

public class RoleUtils {

  public static Role guardianRole() {
    return Role.builder()
        .id(1)
        .name("ROLE_GUARDIAN")
        .build();
  }

  public static Role shelterRole() {
    return Role.builder()
        .id(2)
        .name("ROLE_SHELTER")
        .build();
  }

}
