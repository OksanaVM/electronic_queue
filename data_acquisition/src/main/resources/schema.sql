create table if not exists TICKET_SESSION
(
    ticket_number uuid PRIMARY KEY,
    session_id    uuid PRIMARY KEY
);

create table if not exists TICKET (
    number                 uuid PRIMARY KEY,
    registration_date               TIMESTAMP,
    state VARCHAR(50),
    FOREIGN KEY (number) REFERENCES TICKET_SESSION(ticket_number)
);

create table if not exists SESSION (
     id                 uuid PRIMARY KEY,
     start_date               TIMESTAMP,
     session_status VARCHAR(50),
     FOREIGN KEY (id) REFERENCES TICKET_SESSION(session_id)
);

