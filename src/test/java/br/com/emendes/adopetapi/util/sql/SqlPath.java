package br.com.emendes.adopetapi.util.sql;

public class SqlPath {

  private SqlPath() {}

  public static final String INSERT_SHELTER_SQL_PATH = "/sql/user/shelter/insert-shelter.sql";
  public static final String INSERT_MANY_SHELTERS_SQL_PATH = "/sql/user/shelter/insert-many-shelters.sql";
  public static final String INSERT_GUARDIAN_SQL_PATH = "/sql/user/guardian/insert-guardian.sql";
  public static final String INSERT_MANY_GUARDIANS_SQL_PATH = "/sql/user/guardian/insert-many-guardians.sql";
  public static final String INSERT_GUARDIAN_AND_SHELTER_SQL_PATH = "/sql/user/insert-guardian-and-shelter.sql";
  public static final String INSERT_SHELTER_PET_SQL_PATH = "/sql/pet/insert-pet-shelter.sql";
  public static final String INSERT_SHELTER_PET_GUARDIAN_SQL_PATH = "/sql/pet/insert-pet-shelter-guardian.sql";
}
