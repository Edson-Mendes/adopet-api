package br.com.emendes.adopetapi.repository;

import br.com.emendes.adopetapi.model.entity.Guardian;
import br.com.emendes.adopetapi.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuardianRepository extends JpaRepository<Guardian, Long> {

  Optional<Guardian> findByUserId(Long id);

  Optional<Guardian> findByIdAndUserAndDeletedFalse(Long id, User user);

  Optional<Guardian> findByIdAndDeletedFalse(Long id);

  Page<Guardian> findByDeletedFalse(Pageable pageable);

  Optional<Guardian> findByUser(User user);
}
