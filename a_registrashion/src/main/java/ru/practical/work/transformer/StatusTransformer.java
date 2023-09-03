package ru.practical.work.transformer;


import br.com.rformagio.grpc.server.grpcserver.Status;
import org.springframework.stereotype.Service;
import ru.practical.work.entity.enums.State;

import static ru.practical.work.entity.enums.State.WAITING;

@Service
public class StatusTransformer {
    public State transform(Status status) {
        return switch (status) {
            case WAITING, UNRECOGNIZED -> WAITING;
            case CALLING -> State.CALLING;
            case SERVING -> State.SERVICING;
            case SERVED -> State.SERVICED;
        };
    }

    public Status transformToStatus(State state) {
        return switch (state) {
            case WAITING -> Status.WAITING;
            case CALLING -> Status.CALLING;
            case SERVICING -> Status.SERVING;
            case SERVICED -> Status.SERVED;
        };
    }
}
