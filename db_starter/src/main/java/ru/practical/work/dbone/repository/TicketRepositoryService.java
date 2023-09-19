package ru.practical.work.dbone.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practical.work.dbone.entity.TicketVersion;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.dbone.entity.enums.State;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketRepositoryService {
    private final TicketRepository ticketRepository;
    private final TicketVersionRepository ticketVersionRepository;

    public void save(Ticket ticket) {
        saveVersion(ticket);
        ticketRepository.save(ticket);
    }

    public void saveVersion(Ticket ticket) {
        TicketVersion ticketVersion = new TicketVersion(UUID.randomUUID(), ticket.getNumber(), ticket.getState(),
                LocalDateTime.now());
        ticketVersionRepository.save(ticketVersion);
    }

    public Optional<Ticket> findFirstByState(State state) {
        return ticketRepository.findFirstByState(state);
    }

    public Optional<Ticket> findFirstByStateOrderByNumberAsc(State state) {
        return ticketRepository.findFirstByStateOrderByNumberAsc(state);
    }

    public Optional<Ticket> findById(UUID id) {
        return ticketRepository.findById(id);
    }
}
