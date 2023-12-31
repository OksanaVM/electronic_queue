package ru.practical.work.dbone.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.dbone.entity.enums.State;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Optional<Ticket> findFirstByState(State state);

    Optional<Ticket> findFirstByStateOrderByNumberAsc(State state);
}
