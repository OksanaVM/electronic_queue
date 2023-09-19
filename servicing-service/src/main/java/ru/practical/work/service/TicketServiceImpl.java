package ru.practical.work.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practical.work.dbone.entity.Session;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.dbone.entity.TicketHistory;
import ru.practical.work.dbone.entity.enums.SessionStatus;
import ru.practical.work.dbone.entity.enums.State;
import ru.practical.work.dbone.repository.SessionRepository;
import ru.practical.work.dbone.repository.TicketHistoryRepository;
import ru.practical.work.dbone.repository.TicketRepository;
import ru.practical.work.dbtwo.entity.OldTicket;
import ru.practical.work.dbtwo.repository.OldTicketRepository;
import ru.practical.work.exeption.BadRequestException;
import ru.practical.work.exeption.NotFoundException;
import ru.practical.work.kafka.KafkaProducerService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Component
@Slf4j
public class TicketServiceImpl {

    private final SessionRepository sessionRepository;
    private final KafkaProducerService kafkaProducerService;
    private final TicketRepository ticketRepository;

    private final TicketHistoryRepository ticketHistoryRepository;

    private final OldTicketRepository oldTicketRepository;
    @Value("${kafka.topic.session}")
    private String ticketKafkaTopic;


    @Transactional("dboneTransactionManager")
    public Ticket callTicket() {
        Optional<Ticket> optionalTicket = ticketRepository.findFirstByStateOrderByNumberAsc(State.WAITING);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            if (ticket.getState() == State.WAITING) {
                Session session = sessionRepository.findFirstBySessionStatus(SessionStatus.FREE).orElse(null);
                if (session != null) {
                    ticket.setState(State.CALLING);
                    session.setSessionStatus(SessionStatus.CALL);
                    saveTicketHistory(ticket);
                    ticketRepository.save(ticket);
                    ticket.setSession(session);
                    session.setTicket(ticket);
                    return ticket;
                } else {
                    throw new BadRequestException("No available sessions");
                }
            } else {
                throw new BadRequestException("Ticket is not WAITING");
            }
        } else {
            throw new NotFoundException("No available tickets");
        }
    }


    @Transactional("dboneTransactionManager")
    public Ticket setNewStatus(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Ticket not found for id: " + id));

        Session session = ticket.getSession();
        if (session == null) {
            throw new BadRequestException("Session is null for ticket id: " + id);
        }

        if (ticket.getState() != State.CALLING) {
            throw new BadRequestException("Ticket is not in CALLING state");
        }
        session.setSessionStatus(SessionStatus.SERVICE);
        sessionRepository.save(session);
        ticket.setState(State.SERVICING);
        saveTicketHistory(ticket);
        ticketRepository.save(ticket);
        return ticket;
    }

    @Transactional("dboneTransactionManager")
    public Ticket endServicing(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found for id: " + id));

        if (ticket.getState() != State.SERVICING) {
            throw new BadRequestException("Ticket is not in SERVICING state");
        }

        Session session = ticket.getSession();
        if (session == null || session.getSessionStatus() != SessionStatus.SERVICE) {
            throw new BadRequestException("Invalid Session state for the Ticket");
        }
        ticket.setSession(null);
        session.setSessionStatus(SessionStatus.FREE);
        sessionRepository.save(session);
        ticket.setState(State.SERVICED);
        saveTicketHistory(ticket);
        ticketRepository.save(ticket);
        oldTicketSave(ticket.getNumber());
        ticketRepository.deleteById(ticket.getNumber());
        kafkaProducerService.sendMessage(ticketKafkaTopic, session);
        return ticket;
    }

    @Transactional("dbtwoTransactionManager")
    public void oldTicketSave(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found for id: " + id));
        if (ticket.getState() == State.SERVICED) {
            OldTicket oldTicket = new OldTicket(UUID.randomUUID(), ticket.getNumber(), ticket.getState(),
                    LocalDateTime.now());
            oldTicketRepository.save(oldTicket);
        }
    }


    private void saveTicketHistory(Ticket ticket) {
        TicketHistory ticketHistory = new TicketHistory(UUID.randomUUID(), ticket.getNumber(), ticket.getState(),
                LocalDateTime.now());
        ticketHistoryRepository.save(ticketHistory);
    }
}
