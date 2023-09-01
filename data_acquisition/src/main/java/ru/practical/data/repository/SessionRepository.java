package ru.practical.data.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practical.data.entity.Session;
import ru.practical.data.entity.enums.SessionStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional <Session> findFirstBySessionStatus(SessionStatus status);
}
