package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

  Optional<Guardian> findByUserId(Long id);

}
