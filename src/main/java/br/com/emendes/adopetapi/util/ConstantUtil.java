package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.model.entity.Role;

public class ConstantUtil {

  private ConstantUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static final Role ROLE_GUARDIAN = Role.builder()
      .id(1)
      .name("ROLE_GUARDIAN")
      .build();

  public static final Role ROLE_SHELTER = Role.builder()
      .id(2)
      .name("ROLE_SHELTER")
      .build();

}
