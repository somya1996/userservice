package dev.somya.userservice.repositories;

import dev.somya.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long>{
    Optional<Session> findByTokenAndUserId(String token, Long userId);
}
