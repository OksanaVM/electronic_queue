package ru.practical.work.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practical.work.entity.enums.State;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class TicketDto {

    private UUID number;

    private LocalDateTime registrationDate;

    private State state;

}
