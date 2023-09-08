package ru.practical.work.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practical.work.entity.Session;
import ru.practical.work.entity.Ticket;
import ru.practical.work.entity.enums.SessionStatus;
import ru.practical.work.entity.enums.State;
import ru.practical.work.repository.SessionRepository;
import ru.practical.work.repository.TicketRepository;

import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
@Service
public class KafkaConsumerService {
    private final TicketRepository ticketRepository;
    private final SessionRepository sessionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @KafkaListener(topics = "ticket", groupId = "my-group")
    public void listen(String message) {
        log.info("Received ticket: " + message);
        Ticket ticket = convertJsonToTicket(message);
        Optional<Session> session = sessionRepository.findFirstBySessionStatus(SessionStatus.FREE);
         if(session.isPresent()){
             Session session1 = session.get();
             session1.setSessionStatus(SessionStatus.CALL);
             sessionRepository.save(session1);

             ticket.setSession(session1);
             ticket.setState(State.CALLING);
             ticketRepository.save(ticket);
         }
    }

    @KafkaListener(topics = "queue-distribution-topic", groupId = "my-group")
    public void listenSession(String message) {
        log.info("Received session: " + message);
        Session session = convertJsonToSession(message);
        Optional<Ticket> ticket = ticketRepository.findFirstByState(State.WAITING);
        if(ticket.isPresent()){
            Ticket ticket1 = ticket.get();
            ticket1.setState(State.CALLING);
            ticketRepository.save(ticket1);

            session.setSessionStatus(SessionStatus.CALL);
            session.setTicket(ticket1);
            sessionRepository.save(session);
        }
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