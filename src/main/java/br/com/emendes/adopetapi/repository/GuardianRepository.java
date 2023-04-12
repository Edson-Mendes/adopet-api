package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Guardian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {
}
