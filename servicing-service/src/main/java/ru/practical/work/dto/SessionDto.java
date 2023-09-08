package ru.practical.work.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practical.work.entity.enums.SessionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class SessionDto {
    private UUID id;
    private LocalDateTime startDate;

    private SessionStatus sessionStatus;
}
