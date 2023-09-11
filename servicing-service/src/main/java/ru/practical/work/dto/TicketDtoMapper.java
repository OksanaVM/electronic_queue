package ru.practical.work.dto;

import org.springframework.stereotype.Component;
import ru.practical.work.entity.Ticket;

@Component
public class TicketDtoMapper {
    public TicketDto toDtoTicket(Ticket ticket) {
        return new TicketDto(ticket.getNumber(), ticket.getRegistrationDate(), ticket.getState());
    }

    public Ticket toEntityTicket(TicketDto dto) {
        return new Ticket(dto.getNumber(), dto.getRegistrationDate(), dto.getState());
    }
}
