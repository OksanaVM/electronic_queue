package ru.practical.work.service;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Value;
import ru.practical.work.entity.Ticket;
import ru.practical.work.kafka.KafkaProducerService;
import ru.practical.work.proto.RegisterTicketRequest;
import ru.practical.work.proto.RegisterTicketResponse;
import ru.practical.work.proto.RegistrationServiceGrpc;
import ru.practical.work.repository.TicketRepository;
import ru.practical.work.mapper.TicketMapper;


@GRpcService
@Slf4j
public class TicketServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase {

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    private final KafkaProducerService kafkaProducerService;

    private final String ticketKafkaTopic;


    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
                             KafkaProducerService kafkaProducerService,
                             @Value("${kafka.topic.ticket}") String ticketKafkaTopic) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.kafkaProducerService = kafkaProducerService;
        this.ticketKafkaTopic = ticketKafkaTopic;
    }

    @Override
    public void registerTicket(RegisterTicketRequest request,
                               StreamObserver<RegisterTicketResponse> responseObserver) {
        log.info("registerTicket");
        Ticket ticket = ticketMapper.transformToEntityRequest(request);
        ticketRepository.save(ticket);
        log.info("kafkaProducerService");
        kafkaProducerService.sendMessage(ticketKafkaTopic, ticket);
        log.info("sendMessage");
        RegisterTicketResponse registerTicketResponse = ticketMapper.transformToResponse(ticket);
        log.info("transformToResponse(ticket)");
        responseObserver.onNext(registerTicketResponse);
        responseObserver.onCompleted();
    }

}
