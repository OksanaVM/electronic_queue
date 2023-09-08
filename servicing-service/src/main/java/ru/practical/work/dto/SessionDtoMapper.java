package ru.practical.work.dto;

import org.springframework.stereotype.Component;
import ru.practical.work.entity.Session;

@Component
public class SessionDtoMapper {

    public SessionDto toDto(Session session){
        return new SessionDto(session.getId(), session.getStartDate(), session.getSessionStatus());
    }

    public Session toEntity(SessionDto dto) {
        return new Session(dto.getId(), dto.getStartDate(), dto.getSessionStatus());
    }
}
