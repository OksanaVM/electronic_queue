package ru.practical.registr.entity;

import lombok.*;
import ru.practical.registr.entity.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {
    private UUID number;
    private LocalDateTime registrationDate;
    private State state;

}
