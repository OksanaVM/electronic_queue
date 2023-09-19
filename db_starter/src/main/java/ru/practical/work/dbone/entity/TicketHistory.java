package ru.practical.work.dbone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practical.work.dbone.entity.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "ticket_history")
public class TicketHistory {
    @Id
    @Column(name = "version")
    private UUID version;
    @Column(name = "ticket_number")
    private UUID ticketNumber;
    @Column(name = "ticket_state")
    @Enumerated(EnumType.STRING)
    private State ticketState;

    @Column(name = "ticket_start_state")
    private LocalDateTime ticketStart;
}
