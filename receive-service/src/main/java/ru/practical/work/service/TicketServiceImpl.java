package ru.practical.work.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.kafka.KafkaProducerService;
import ru.practical.work.mapper.TicketMapper;
import ru.practical.work.proto.RegisterTicketRequest;
import ru.practical.work.proto.RegisterTicketResponse;
import ru.practical.work.proto.RegistrationServiceGrpc;
import ru.practical.work.dbone.repository.TicketRepositoryService;


@Component
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class TicketServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase {

    private final TicketRepositoryService ticketRepository;

    private final TicketMapper ticketMapper;

    private final KafkaProducerService kafkaProducerService;

    @Value("${kafka.topic.ticket}")
    private String ticketKafkaTopic;


    @Transactional
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
