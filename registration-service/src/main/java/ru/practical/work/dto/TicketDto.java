package ru.practical.work.dto;

import lombok.*;
import ru.practical.work.dto.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
    private UUID number;
    private LocalDateTime registrationDate;
    private State state;

}
