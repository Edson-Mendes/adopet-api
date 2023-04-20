package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Shelter;
import br.com.emendes.adopetapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

  Optional<Shelter> findByUserId(Long id);

  Optional<Shelter> findByIdAndUser(Long id, User user);

}
