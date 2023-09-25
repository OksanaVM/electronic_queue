package ru.practical.work.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.dto.TicketDto;
import ru.practical.work.dto.TicketDtoMapper;
import ru.practical.work.service.TicketServiceImpl;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/service/ticket")
public class TicketController {

    private final TicketDtoMapper ticketDtoMapper;

    private final TicketServiceImpl ticketService;


    @PostMapping("/call")
    public ResponseEntity<TicketDto> callService() {
        Ticket ticket = ticketService.callTicket();
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketDtoMapper.toDtoTicket(ticket));
    }

    @PostMapping("/start")
    public ResponseEntity<TicketDto> startService(@RequestParam UUID ticketId) {
        Ticket ticket = ticketService.setNewStatus(ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(ticketDtoMapper.toDtoTicket(ticket));
    }

    @PostMapping("/end")
    public ResponseEntity<TicketDto> endService(@RequestParam UUID ticketId) {
        Ticket ticket = ticketService.endServicing(ticketId);
        return ResponseEntity.status(HttpStatus.OK).body(ticketDtoMapper.toDtoTicket(ticket));
    }

}
