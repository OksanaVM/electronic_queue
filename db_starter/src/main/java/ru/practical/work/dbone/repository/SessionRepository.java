package ru.practical.work.dbone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practical.work.dbone.entity.Session;
import ru.practical.work.dbone.entity.enums.SessionStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findFirstBySessionStatus(SessionStatus status);

    Optional<Session> findFirstBySessionStatusOrderByStartDateAsc(SessionStatus status);


}
