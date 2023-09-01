package ru.practical.data;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practical.data.entity.Ticket;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Ticket> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Ticket> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Ticket message) {
        kafkaTemplate.send(topic, message);
    }
}


