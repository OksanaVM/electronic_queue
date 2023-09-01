package ru.practical.data;

import br.com.rformagio.grpc.server.grpcserver.RegisterTicketRequest;
import br.com.rformagio.grpc.server.grpcserver.RegisterTicketResponse;
import br.com.rformagio.grpc.server.grpcserver.RegistrationServiceGrpc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import org.springframework.beans.factory.annotation.Value;
import ru.practical.data.entity.Ticket;
import ru.practical.data.repository.TicketRepository;
import ru.practical.data.transform.TicketTransform;



@GRpcService
@Slf4j
public class TicketServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase{

    private final TicketRepository ticketRepository;

    private final TicketTransform ticketTransform;

    private final KafkaProducerService kafkaProducerService; // Внедряем сервис для отправки данных в Kafka

    private final String ticketKafkaTopic;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TicketServiceImpl(TicketRepository ticketRepository, TicketTransform ticketTransform,
                             KafkaProducerService kafkaProducerService,
                             @Value("${kafka.topic.ticket}")String ticketKafkaTopic) {
        this.ticketRepository = ticketRepository;
        this.ticketTransform = ticketTransform;
        this.kafkaProducerService = kafkaProducerService;
        this.ticketKafkaTopic = ticketKafkaTopic;
    }

    @Override
    public void registerTicket(RegisterTicketRequest request,
                               StreamObserver<RegisterTicketResponse> responseObserver) {
        log.info("registerTicket");
        Ticket ticket = ticketTransform.transformToEntityRequest(request);
        ticketRepository.save(ticket);

        // Отправляем информацию о билете в Kafka
        log.info("kafkaProducerService");// Предположим, что есть метод для преобразования билета в JSON
        kafkaProducerService.sendMessage(ticketKafkaTopic, ticket);
        log.info("sendMessage");
        RegisterTicketResponse registerTicketResponse = ticketTransform.transformToResponse(ticket);
        log.info("transformToResponse(ticket)");
        responseObserver.onNext(registerTicketResponse);
        responseObserver.onCompleted();
    }

    private String transformTicketToJson(Ticket ticket) {
        try {
            return objectMapper.writeValueAsString(ticket);
        } catch (JsonProcessingException e) {
            log.error("transformTicketToJson: "+e);
            throw new RuntimeException("Error serializing Ticket object to JSON", e);
        }
    }

}
