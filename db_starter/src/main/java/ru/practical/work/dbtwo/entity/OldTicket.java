package ru.practical.work.dbtwo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.practical.work.dbone.entity.Ticket;
import ru.practical.work.dbone.entity.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "old_ticket")
public class OldTicket {
    @Id
    private UUID id;

    @Column(name = "ticket_number")
    private UUID ticket_number;

    @Column(name = "ticket_state")
    @Enumerated(EnumType.STRING)
    private State ticketState;

    @Column(name = "ticket_start")
    private LocalDateTime ticketStart;


    public OldTicket(Ticket ticket) {
        this.ticket_number = ticket.getNumber();
        this.ticketState = ticket.getState();
        this.ticketStart = ticket.getRegistrationDate();
    }
}
