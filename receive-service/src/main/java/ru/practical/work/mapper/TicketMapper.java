package ru.practical.work.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.proto.RegisterTicketRequest;
import ru.practical.work.proto.RegisterTicketResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketMapper {

    private final StatusMapper statusMapper;

    public Ticket transformToEntityRequest(RegisterTicketRequest request) {
        return new Ticket(UUID.fromString(request.getNumber()), LocalDateTime.now(),
                statusMapper.transform(request.getStatus()));
    }

    public RegisterTicketResponse transformToResponse(Ticket ticket) {
        return RegisterTicketResponse.newBuilder()
                .setNumber(ticket.getNumber().toString())
                .setRegistrationTime(ticket.getRegistrationDate().toString())
                .setStatus(statusMapper.transformToStatus(ticket.getState()))
                .build();
    }
}
