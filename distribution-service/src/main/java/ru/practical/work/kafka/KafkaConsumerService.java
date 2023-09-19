package ru.practical.work.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practical.work.dbone.entity.Session;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.dbone.entity.TicketHistory;
import ru.practical.work.dbone.entity.enums.SessionStatus;
import ru.practical.work.dbone.entity.enums.State;
import ru.practical.work.dbone.repository.SessionRepository;
import ru.practical.work.dbone.repository.TicketHistoryRepository;
import ru.practical.work.dbone.repository.TicketRepository;

import java.time.LocalDateTime;
import java.util.UUID;


@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaConsumerService {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;

    private final TicketHistoryRepository ticketHistoryRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "ticket", groupId = "my-group")
    public void listenTicket(String message) {
        log.info("Received ticket: " + message);
        Ticket ticket = convertJsonToTicket(message);

        sessionRepository.findFirstBySessionStatus(SessionStatus.FREE)
                .ifPresent(callSession -> {
                    callSession.setSessionStatus(SessionStatus.CALL);
                    sessionRepository.save(callSession);
                    ticket.setSession(callSession);
                    ticket.setState(State.CALLING);
                    saveTicketHistory(ticket);
                    ticketRepository.save(ticket);
                });
    }

    @KafkaListener(topics = "queue-distribution-topic", groupId = "my-group")
    public void listenSession(String message) {
        log.info("Received session: " + message);
        Session session = convertJsonToSession(message);

        ticketRepository.findFirstByState(State.WAITING)
                .ifPresent(callTicket -> {
                    callTicket.setState(State.CALLING);
                    callTicket.setSession(session);
                    saveTicketHistory(callTicket);
                    ticketRepository.save(callTicket);
                    session.setTicket(callTicket);
                    session.setSessionStatus(SessionStatus.CALL);
                    sessionRepository.save(session);
                });
    }

    private Ticket convertJsonToTicket(String message) {
        try {
            return objectMapper.readValue(message, Ticket.class);
        } catch (JsonProcessingException e) {
            log.error("Can not pars ticket: " + message);
            throw new RuntimeException(e);
        }
    }

    private Session convertJsonToSession(String message) {
        try {
            return objectMapper.readValue(message, Session.class);
        } catch (JsonProcessingException e) {
            log.error("Can not pars session: " + message);
            throw new RuntimeException(e);
        }
    }

    private void saveTicketHistory(Ticket ticket) {
        TicketHistory ticketHistory = new TicketHistory(UUID.randomUUID(), ticket.getNumber(), ticket.getState(),
                LocalDateTime.now());
        ticketHistoryRepository.save(ticketHistory);
    }
}