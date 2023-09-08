package ru.practical.work.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practical.work.entity.enums.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "SESSION")
public class Session {
    @Id
    private UUID id;
    @Column(name = "start_date")
    @JsonSerialize(converter = LocalDateTimeSerializer.class)
    @JsonDeserialize(converter = LocalDateTimeDeserializer.class)
    private LocalDateTime startDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_status")
    private SessionStatus sessionStatus;
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(name = "TICKET_SESSION",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_number"))
    private Ticket ticket;

    public Session(UUID id, LocalDateTime startDate, SessionStatus sessionStatus) {
        this.id = id;
        this.startDate = startDate;
        this.sessionStatus = sessionStatus;
    }

}
