package ru.practical.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practical.data.entity.enums.SessionStatus;


import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "SESSION")
public class Session {
    @Id
    private UUID id;
    @Column(name = "start_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_status")
    private SessionStatus sessionStatus;
    @OneToOne
    @JoinTable(name = "TICKET_SESSION",
            joinColumns = @JoinColumn(name = "ticket_number"),
            inverseJoinColumns = @JoinColumn(name = "session_id"))
    private Ticket ticket;
}
