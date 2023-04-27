package br.com.emendes.adopetapi.util.sql;

public class SqlPath {

  private SqlPath() {}

  // Relacionados com user
  public static final String INSERT_SHELTER_SQL_PATH = "/sql/user/shelter/insert-shelter.sql";
  public static final String INSERT_MANY_SHELTERS_SQL_PATH = "/sql/user/shelter/insert-many-shelters.sql";
  public static final String INSERT_GUARDIAN_SQL_PATH = "/sql/user/guardian/insert-guardian.sql";
  public static final String INSERT_MANY_GUARDIANS_SQL_PATH = "/sql/user/guardian/insert-many-guardians.sql";
  public static final String INSERT_GUARDIAN_AND_SHELTER_SQL_PATH = "/sql/user/insert-guardian-and-shelter.sql";

  // Relacionados com pet
  public static final String INSERT_SHELTER_PET_SQL_PATH = "/sql/pet/insert-pet-shelter.sql";
  public static final String INSERT_SHELTER_PET_GUARDIAN_SQL_PATH = "/sql/pet/insert-pet-shelter-guardian.sql";
  public static final String INSERT_MANY_SHELTERS_AND_PETS_SQL_PATH = "/sql/pet/insert-many-shelters-and-pets.sql";

  // Relacionados com adoption
  public static final String INSERT_PET_SHELTER_GUARDIAN_ADOPTION_SQL_PATH = "/sql/adoption/insert-pet-shelter-guardian-adoption.sql";
}
