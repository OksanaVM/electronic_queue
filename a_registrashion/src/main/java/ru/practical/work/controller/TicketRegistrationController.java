package ru.practical.work.controller;

import br.com.rformagio.grpc.server.grpcserver.RegisterTicketRequest;
import br.com.rformagio.grpc.server.grpcserver.RegisterTicketResponse;
import br.com.rformagio.grpc.server.grpcserver.RegistrationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practical.work.entity.Ticket;
import ru.practical.work.transformer.TicketTransformer;
import ru.practical.work.entity.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
public class TicketRegistrationController {
    private final TicketTransformer ticketTransformer;

    @PostMapping("/register")
    public ResponseEntity<Ticket> registerTicket() {
        Ticket ticket = new Ticket(UUID.randomUUID(), LocalDateTime.now(), State.WAITING);


        RegisterTicketRequest grpcRequest = ticketTransformer.transformToRequest(ticket);

        int grpcServerPort = 6565;
        String grpcServerHost = "localhost";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                .usePlaintext()
                .build();

        RegistrationServiceGrpc.RegistrationServiceBlockingStub registrationClient =
                RegistrationServiceGrpc.newBlockingStub(channel);

        RegisterTicketResponse grpcResponse = registrationClient.registerTicket(grpcRequest);


        return ResponseEntity.ok(ticketTransformer.transformToEntityResponse(grpcResponse));
    }

}
