package ru.practical.registr.transformer;

import br.com.rformagio.grpc.server.grpcserver.RegisterTicketRequest;
import br.com.rformagio.grpc.server.grpcserver.RegisterTicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practical.registr.entity.Ticket;


import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketTransformer {

    private final StatusTransformer statusTransformer;

    public Ticket transformToEntityResponse(RegisterTicketResponse response) {
        return new Ticket(UUID.fromString(response.getNumber()), LocalDateTime.now(), statusTransformer.transform(response.getStatus()));
    }

    public RegisterTicketRequest transformToRequest(Ticket ticket) {
        return RegisterTicketRequest.newBuilder()
                .setNumber(ticket.getNumber().toString())
                .setRegistrationTime(ticket.getRegistrationDate().getNano())
                .setStatus(statusTransformer.transformToStatus(ticket.getState()))
                .build();
    }
}
