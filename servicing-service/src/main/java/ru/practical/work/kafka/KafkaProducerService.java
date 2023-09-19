package ru.practical.work.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practical.work.dbone.entity.Session;


@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Session> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Session> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Session message) {
        kafkaTemplate.send(topic, message);
    }
}


