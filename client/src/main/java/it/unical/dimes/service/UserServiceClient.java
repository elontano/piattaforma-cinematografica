package it.unical.dimes.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import it.unical.dimes.protocol.UserRequest;
import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.protocol.UserServiceGrpc;

import java.util.logging.Logger;

public class UserServiceClient {
    private final Logger logger = Logger.getLogger(UserServiceClient.class.getName());

    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;
    private final ManagedChannel channel;

    public UserServiceClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()//modificare altrimenti i dati viaggiano in chiaro
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
        } catch (StatusRuntimeException e){
            if(e.getStatus().getCode() == Status.Code.UNAUTHENTICATED){
                return UserResponse.newBuilder().setSuccess(false).setMessage("Invalid Credentials.").build();
            }
            // Altri errori
            return UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Server error: " + e.getStatus().getDescription())
                    .build();
        }
        catch (Exception e) {
            logger.severe("Unexpected login error: " + e.getMessage());
            return UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Internal error: "+e.getMessage())
                    .build();
        }
    }

    public UserResponse register(String username, String password) {
        UserRequest request = UserRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();

        try {
            return blockingStub.register(request);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.ALREADY_EXISTS.getCode()) {
                return UserResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Username already exists")
                        .build();
            }
            return UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Server Error: " + e.getStatus().getDescription())
                    .build();
        }catch (Exception e) {
            logger.severe("Unexpected Registration Error: " + e.getMessage());
            return UserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Registration failed. " + e.getMessage())
                    .build();
        }
    }

    public void shutdown() {
        channel.shutdown();
    }
}
