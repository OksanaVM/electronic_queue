package ru.practical.work.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.dbone.entity.TicketHistory;
import ru.practical.work.dbone.repository.TicketHistoryRepository;
import ru.practical.work.dbone.repository.TicketRepository;
import ru.practical.work.kafka.KafkaProducerService;
import ru.practical.work.mapper.TicketMapper;
import ru.practical.work.proto.RegisterTicketRequest;
import ru.practical.work.proto.RegisterTicketResponse;
import ru.practical.work.proto.RegistrationServiceGrpc;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;


@Component
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class TicketServiceImpl extends RegistrationServiceGrpc.RegistrationServiceImplBase {

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    private final TicketHistoryRepository ticketHistoryRepository;

    private final KafkaProducerService kafkaProducerService;

    @Value("${kafka.topic.ticket}")
    private String ticketKafkaTopic;


    @Transactional("dboneTransactionManager")
    @Override
    public void registerTicket(RegisterTicketRequest request,
                               StreamObserver<RegisterTicketResponse> responseObserver) {
        log.info("registerTicket");
        Ticket ticket = ticketMapper.transformToEntityRequest(request);
        saveTicketHistory(ticket);
        ticketRepository.save(ticket);
        log.info("kafkaProducerService");
        Instant timestamp = Instant.now();
        kafkaProducerService.sendMessage(ticketKafkaTopic, ticket);
        log.info("sendMessage");
        Instant timestamp2 = Instant.now();
        RegisterTicketResponse registerTicketResponse = ticketMapper.transformToResponse(ticket);
        log.info("transformToResponse(ticket)");
        responseObserver.onNext(registerTicketResponse);
        responseObserver.onCompleted();
        System.out.println(timestamp);
        System.out.println(timestamp2);
    }

    private void saveTicketHistory(Ticket ticket) {
        TicketHistory ticketHistory = new TicketHistory(UUID.randomUUID(), ticket.getNumber(), ticket.getState(),
                LocalDateTime.now());
        Instant timestamp = Instant.now();
        System.out.println(timestamp);
        ticketHistoryRepository.save(ticketHistory);
    }

}
