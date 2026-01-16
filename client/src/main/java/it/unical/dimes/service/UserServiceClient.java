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

    public UserResponse login(String username,String password) {

        UserRequest request = UserRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        try {
            return blockingStub.login(request);
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Errore di comunicazione "+e.getMessage())
                    .build();
        }
    }

    public UserResponse register(String username, String password){

        UserRequest request = UserRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        try {
            return blockingStub.register(request);
        }catch (Exception e ){
            System.err.println("Registrazion error: "+e.getMessage());
            e.printStackTrace();
            return UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Errore registrazione "+e.getMessage())
                    .build();
        }
    }

    public void shutdown() {
        channel.shutdown();
    }

}
