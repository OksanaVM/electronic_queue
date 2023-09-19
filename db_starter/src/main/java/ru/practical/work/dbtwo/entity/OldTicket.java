package ru.practical.work.dbtwo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practical.work.dbone.entity.enums.SessionStatus;

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
    private UUID version;
    @Column(name = "ticket_number")
    private UUID sessionNumber;
    @Column(name = "ticket_status")
    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;

    @Column(name = "ticket_start_state")
    private LocalDateTime sessionStart;
}
