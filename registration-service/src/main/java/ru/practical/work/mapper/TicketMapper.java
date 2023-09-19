package ru.practical.work.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practical.work.dto.TicketDto;
import ru.practical.work.proto.RegisterTicketRequest;
import ru.practical.work.proto.RegisterTicketResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketMapper {

    private final StatusMapper statusMapper;

    public TicketDto transformToEntityResponse(RegisterTicketResponse response) {
        return new TicketDto(UUID.fromString(response.getNumber()), LocalDateTime.now(), statusMapper.transform(response.getStatus()));
    }

    public RegisterTicketRequest transformToRequest(TicketDto ticketDto) {
//        ZonedDateTime zdt = ZonedDateTime.of(ticketDto.getRegistrationDate(), ZoneId.systemDefault());
//        long date = zdt.toInstant().toEpochMilli();
        return RegisterTicketRequest.newBuilder()
                .setNumber(ticketDto.getNumber().toString())
                .setRegistrationTime(ticketDto.getRegistrationDate().toString())
                .setStatus(statusMapper.transformToStatus(ticketDto.getState()))
                .build();
    }
}
