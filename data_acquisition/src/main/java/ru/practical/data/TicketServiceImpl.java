package ru.practical.data;

import br.com.rformagio.grpc.server.grpcserver.RegisterTicketRequest;
import br.com.rformagio.grpc.server.grpcserver.RegisterTicketResponse;
import br.com.rformagio.grpc.server.grpcserver.RegistrationServiceGrpc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.practical.data.entity.Ticket;
import ru.practical.data.repository.TicketRepository;
import ru.practical.data.transform.TicketTransform;


@RequiredArgsConstructor
@GRpcService
@Slf4j
public class TicketServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase{

    private final TicketRepository ticketRepository;

    private final TicketTransform ticketTransform;
    @Autowired
    private KafkaProducerService kafkaProducerService; // Внедряем сервис для отправки данных в Kafka

    @Value("${kafka.topic.ticket}") // Имя темы Kafka для билетов из конфигурационного файла
    private String ticketKafkaTopic;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void registerTicket(RegisterTicketRequest request,
                               StreamObserver<RegisterTicketResponse> responseObserver) {
        log.info("registerTicket");
        Ticket ticket = ticketTransform.transformToEntityRequest(request);
        ticketRepository.save(ticket);

        // Отправляем информацию о билете в Kafka
        String ticketMessage = transformTicketToJson(ticket); // Предположим, что есть метод для преобразования билета в JSON
        kafkaProducerService.sendMessage(ticketKafkaTopic, ticketMessage);
        responseObserver.onNext(ticketTransform.transformToResponse(ticket));
        responseObserver.onCompleted();
    }

    private String transformTicketToJson(Ticket ticket) {
        try {
            return objectMapper.writeValueAsString(ticket);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Ticket object to JSON", e);
        }
    }

}
