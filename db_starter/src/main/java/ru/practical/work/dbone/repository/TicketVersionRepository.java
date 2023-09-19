package ru.practical.work.dbone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practical.work.dbone.entity.TicketVersion;

import java.util.UUID;

@Repository
public interface TicketVersionRepository extends JpaRepository<TicketVersion, UUID> {
}
