package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}
