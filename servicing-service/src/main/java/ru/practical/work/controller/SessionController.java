package ru.practical.work.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practical.work.dbone.entity.Session;
import ru.practical.work.dto.SessionDto;
import ru.practical.work.dto.SessionDtoMapper;
import ru.practical.work.service.SessionServiceImpl;

@RestController
@RequestMapping("/service/session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionServiceImpl sessionServiceImpl;
    private final SessionDtoMapper sessionDtoMapper;


    @PostMapping("/create")
    public ResponseEntity<SessionDto> createSession() {
        Session session = sessionServiceImpl.createNewSession();
        return ResponseEntity.ok(sessionDtoMapper.toDtoSession(session));
    }

}
