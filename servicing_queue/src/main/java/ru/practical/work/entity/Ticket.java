package ru.practical.work.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import ru.practical.work.entity.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TICKET")
public class Ticket {
    @Id
    private UUID number;
    @Column(name = "registration_date")
    @JsonSerialize(converter = LocalDateTimeSerializer.class)
    @JsonDeserialize(converter = LocalDateTimeDeserializer.class)
    private LocalDateTime registrationDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;
    @OneToOne
    @JsonIgnore
    @JoinTable(name = "TICKET_SESSION",
            joinColumns = @JoinColumn(name = "ticket_number"),
            inverseJoinColumns = @JoinColumn(name = "session_id"))
    private Session session;

    public Ticket(UUID number, LocalDateTime registrationDate, State state) {
        this.number = number;
        this.registrationDate = registrationDate;
        this.state = state;
    }
}

