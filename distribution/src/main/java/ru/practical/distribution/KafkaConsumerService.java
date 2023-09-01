package ru.practical.distribution;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.practical.distribution.entity.Session;
import ru.practical.distribution.entity.Ticket;
import ru.practical.distribution.entity.enums.SessionStatus;
import ru.practical.distribution.repository.SessionRepository;
import ru.practical.distribution.repository.TicketRepository;

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
        Ticket ticket = convertJsonToTicket(message);
        log.info("Received message: " + message);
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