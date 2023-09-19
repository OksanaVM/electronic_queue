package ru.practical.work.dbtwo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practical.work.dbone.entity.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "old_ticket_history")
public class OldTicketHistory {
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
