package ru.practical.data.transform;


import br.com.rformagio.grpc.server.grpcserver.RegisterTicketRequest;
import br.com.rformagio.grpc.server.grpcserver.RegisterTicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practical.data.entity.Ticket;


import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketTransform {

    private final StatusTransform statusTransform;

    public Ticket transformToEntityRequest(RegisterTicketRequest request) {
        return new Ticket(UUID.fromString(request.getNumber()), LocalDateTime.now(), statusTransform.transform(request.getStatus()));
    }
    public RegisterTicketResponse transformToResponse(Ticket ticket) {
        return RegisterTicketResponse.newBuilder()
                .setNumber(ticket.getNumber().toString())
                .setRegistrationTime(ticket.getRegistrationDate().getNano())
                .setStatus(statusTransform.transformToStatus(ticket.getState()))
                .build();
    }
}
