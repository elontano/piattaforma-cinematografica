package it.unical.dimes.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.unical.dimes.protocol.UserRequest;
import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.protocol.UserServiceGrpc;

public class UserServiceClient {

    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;
    private final ManagedChannel channel;

    public UserServiceClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = UserServiceGrpc.newBlockingStub(this.channel);
    }

    public UserResponse login(String username) {

        UserRequest request = UserRequest.newBuilder()
                .setUsername(username)
                .build();

        try {
            return blockingStub.login(request);
        } catch (Exception e) {
            System.err.println("RPC aaa: " + e.getMessage());
            return null;
        }
    }

    public void shutdown() {
        channel.shutdown();
    }

}
