package ru.practical.work.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practical.work.entity.Session;
import ru.practical.work.entity.Ticket;
import ru.practical.work.entity.enums.SessionStatus;
import ru.practical.work.entity.enums.State;
import ru.practical.work.repository.SessionRepository;
import ru.practical.work.repository.TicketRepository;


@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaConsumerService {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
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
}