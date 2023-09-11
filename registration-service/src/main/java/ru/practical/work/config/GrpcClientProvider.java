package ru.practical.work.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrpcClientProvider {
    private final ManagedChannel channel;

    public GrpcClientProvider(
            @Value("${grpc.server.host}") String grpcServerHost,
            @Value("${grpc.server.port}") int grpcServerPort
    ) {
        this.channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                .usePlaintext()
                .build();
    }

    public ManagedChannel getChannel() {
        return channel;
    }
}