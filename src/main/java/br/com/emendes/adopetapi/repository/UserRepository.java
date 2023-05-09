package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static br.com.emendes.adopetapi.util.ConstantUtil.USERS_CACHE_NAME;

public interface UserRepository extends JpaRepository<User, Long> {

  @Cacheable(USERS_CACHE_NAME)
  Optional<User> findByEmailAndEnabledTrue(String email);

}
