package br.com.emendes.adopetapi.util;

import br.com.emendes.adopetapi.model.entity.Role;

public class ConstantUtil {

  private ConstantUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static final String ROLE_GUARDIAN_NAME = "ROLE_GUARDIAN";
  public static final String ROLE_SHELTER_NAME = "ROLE_SHELTER";

  public static final Role ROLE_GUARDIAN = Role.builder()
      .id(1)
      .name(ROLE_GUARDIAN_NAME)
      .build();

  public static final Role ROLE_SHELTER = Role.builder()
      .id(2)
      .name(ROLE_SHELTER_NAME)
      .build();

}
