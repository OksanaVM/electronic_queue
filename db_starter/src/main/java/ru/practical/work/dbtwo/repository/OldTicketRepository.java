package ru.practical.work.dbtwo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practical.work.dbtwo.entity.OldTicket;


import java.util.UUID;

@Repository
public interface OldTicketRepository extends JpaRepository<OldTicket, UUID> {
}
