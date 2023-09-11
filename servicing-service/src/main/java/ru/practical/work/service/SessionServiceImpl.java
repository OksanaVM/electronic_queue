package ru.practical.work.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practical.work.entity.Session;
import ru.practical.work.entity.enums.SessionStatus;
import ru.practical.work.kafka.KafkaProducerService;
import ru.practical.work.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@Component
@RequiredArgsConstructor
public class SessionServiceImpl {

    private final SessionRepository sessionRepository;
    private final KafkaProducerService kafkaProducerService;
    @Value("${kafka.topic.session}")
    private String ticketKafkaTopic;


    @Transactional
    public Session createNewSession() {
        Session session = new Session(UUID.randomUUID(), LocalDateTime.now(), SessionStatus.FREE);
        return saveSession(session);
    }

    public Session saveSession(Session session) {
        Session newSession = sessionRepository.save(session);
        kafkaProducerService.sendMessage(ticketKafkaTopic, session);
        return newSession;
    }
}
