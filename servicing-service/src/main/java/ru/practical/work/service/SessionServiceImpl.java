package ru.practical.work.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practical.work.entity.Session;
import ru.practical.work.entity.Ticket;
import ru.practical.work.entity.enums.SessionStatus;
import ru.practical.work.entity.enums.State;
import ru.practical.work.exeption.NotFoundException;
import ru.practical.work.kafka.KafkaProducerService;
import ru.practical.work.repository.SessionRepository;
import ru.practical.work.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
public class SessionServiceImpl {

    private final SessionRepository sessionRepository;
    private final KafkaProducerService kafkaProducerService;
    private final String ticketKafkaTopic;
    private final TicketRepository ticketRepository;

    public SessionServiceImpl(SessionRepository sessionRepository,
                              KafkaProducerService kafkaProducerService,
                              @Value("${kafka.topic.session}") String ticketKafkaTopic, TicketRepository ticketRepository) {
        this.sessionRepository = sessionRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.ticketKafkaTopic = ticketKafkaTopic;
        this.ticketRepository = ticketRepository;
    }

    public Session createNewSession() {
        Session session = new Session(UUID.randomUUID(), LocalDateTime.now(), SessionStatus.FREE);
        return saveSession(session);
    }


    public Session endServicing(UUID id) {
        Session session = sessionRepository.getReferenceById(id);

        return Optional.of(session)
                .map(s -> {
                    Ticket ticket = s.getTicket();

                    return Optional.ofNullable(ticket)
                            .map(t -> {
                                t.setState(State.SERVICED);
                                ticketRepository.save(t);
                                s.setSessionStatus(SessionStatus.FREE);
                                s.setTicket(null);
                                return saveSession(s);
                            })
                            .orElseThrow(() -> new NotFoundException("Ticket is null"));
                })
                .orElseThrow(() -> new NotFoundException("Session is null"));
    }


    public Session setNewStatus(UUID id, SessionStatus status) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found for id: " + id));

        Ticket ticket = session.getTicket();
        if (status == SessionStatus.SERVICE && ticket == null) {
            throw new IllegalArgumentException("Ticket is null for session id: " + id);
        }

        if (status == SessionStatus.SERVICE) {
            ticket.setState(State.SERVICING);
            ticketRepository.save(ticket);
        }

        session.setSessionStatus(status);
        return saveSession(session);
    }


    public Session saveSession(Session session) {
        Session newSession = sessionRepository.save(session);
        kafkaProducerService.sendMessage(ticketKafkaTopic, session);
        return newSession;
    }
}
