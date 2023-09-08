package ru.practical.work.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practical.work.dto.SessionDto;
import ru.practical.work.dto.SessionDtoMapper;
import ru.practical.work.entity.Session;
import ru.practical.work.entity.enums.SessionStatus;
import ru.practical.work.service.SessionServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class SessionTicketController {
    private final SessionServiceImpl sessionServiceImpl;
    private final SessionDtoMapper mapper;

    @PostMapping("/call")
    public ResponseEntity<SessionDto> callService() {
        Session session = sessionServiceImpl.createNewSession();
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(session));
    }

    @PostMapping("/start")
    public ResponseEntity<SessionDto> startService(@RequestParam UUID sessionId) {
        Session session = sessionServiceImpl.setNewStatus(sessionId, SessionStatus.SERVICE);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(session));
    }

    @PostMapping("/end")
    public ResponseEntity<SessionDto> endService(@RequestParam UUID sessionId) {
        Session session = sessionServiceImpl.endServicing(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toDto(session));
    }

}
