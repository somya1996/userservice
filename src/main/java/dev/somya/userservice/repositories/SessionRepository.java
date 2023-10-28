package dev.somya.userservice.repositories;

import dev.somya.userservice.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long>{
}
