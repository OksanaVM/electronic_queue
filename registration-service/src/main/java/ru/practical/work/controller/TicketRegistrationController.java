package ru.practical.work.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practical.work.config.GrpcClientProvider;
import ru.practical.work.dto.TicketDto;
import ru.practical.work.dto.enums.State;
import ru.practical.work.mapper.TicketMapper;
import ru.practical.work.proto.RegisterTicketRequest;
import ru.practical.work.proto.RegisterTicketResponse;
import ru.practical.work.proto.RegistrationServiceGrpc;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@Component
@AllArgsConstructor
public class TicketRegistrationController {
    private TicketMapper ticketMapper;

    private GrpcClientProvider grpcClientProvider;


    @PostMapping("/register/ticket")
    public ResponseEntity<TicketDto> registerTicket() {
        TicketDto ticketDto = new TicketDto(UUID.randomUUID(), LocalDateTime.now(), State.WAITING);
        RegisterTicketRequest grpcRequest = ticketMapper.transformToRequest(ticketDto);
        try {
            RegistrationServiceGrpc.RegistrationServiceBlockingStub registrationClient =
                    RegistrationServiceGrpc.newBlockingStub(grpcClientProvider.getChannel());

            RegisterTicketResponse grpcResponse = registrationClient.registerTicket(grpcRequest);

            return ResponseEntity.ok(ticketMapper.transformToEntityResponse(grpcResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
