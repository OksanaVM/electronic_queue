package ru.practical.work;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practical.work.entity.Session;
import ru.practical.work.entity.Ticket;
import ru.practical.work.entity.enums.SessionStatus;
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
        log.info("Received message: " + message);
        Ticket ticket = convertJsonToTicket(message);
        Optional<Session> session = sessionRepository.findFirstBySessionStatus(SessionStatus.FREE);
         if(session.isPresent()){
             ticket.setSession(session.get());
             ticketRepository.save(ticket);
         } else {
             //todo
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
}