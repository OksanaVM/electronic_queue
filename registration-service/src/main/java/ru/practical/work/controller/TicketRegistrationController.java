package ru.practical.work.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practical.work.dto.TicketDto;
import ru.practical.work.dto.enums.State;
import ru.practical.work.proto.RegisterTicketRequest;
import ru.practical.work.proto.RegisterTicketResponse;
import ru.practical.work.proto.RegistrationServiceGrpc;
import ru.practical.work.mapper.TicketMapper;

import java.time.LocalDateTime;
import java.util.UUID;



@RestController
@Component
public class TicketRegistrationController {
    private final TicketMapper ticketMapper;

    private final String grpcServerHost;
    private final int grpcServerPort;

    public TicketRegistrationController(TicketMapper ticketMapper,
                                        @Value("${grpc.server.host}")String grpcServerHost,
                                        @Value("${grpc.server.port}")int grpcServerPort) {
        this.ticketMapper = ticketMapper;
        this.grpcServerHost = grpcServerHost;
        this.grpcServerPort = grpcServerPort;
    }


    @PostMapping("/register/ticket")
    public ResponseEntity<TicketDto> registerTicket() {
        TicketDto ticketDto = new TicketDto(UUID.randomUUID(), LocalDateTime.now(), State.WAITING);

        RegisterTicketRequest grpcRequest = ticketMapper.transformToRequest(ticketDto);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                .usePlaintext()
                .build();

        RegistrationServiceGrpc.RegistrationServiceBlockingStub registrationClient =
                RegistrationServiceGrpc.newBlockingStub(channel);

        RegisterTicketResponse grpcResponse = registrationClient.registerTicket(grpcRequest);


        return ResponseEntity.ok(ticketMapper.transformToEntityResponse(grpcResponse));
    }

}
