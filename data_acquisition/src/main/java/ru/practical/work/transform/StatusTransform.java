package ru.practical.work.transform;


import br.com.rformagio.grpc.server.grpcserver.Status;
import org.springframework.stereotype.Service;
import ru.practical.work.entity.enums.State;

@Service
public class StatusTransform {
    public State transform(Status status) {
        return switch (status) {
            case WAITING, UNRECOGNIZED -> State.WAITING;
            case CALLING -> State.CALLING;
            case SERVING -> State.SERVICING;
            case  SERVED -> State.SERVICED;
        };

    }

    public Status transformToStatus(State state) {
        return switch (state) {
            case WAITING -> Status.WAITING;
            case CALLING -> Status.CALLING;
            case SERVICING -> Status.SERVING;
            case  SERVICED-> Status.SERVED;
        };

    }
}