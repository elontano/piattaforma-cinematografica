package it.unical.dimes.controller;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.dimes.entities.User;
import it.unical.dimes.protocol.UserRequest;
import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.protocol.UserServiceGrpc;
import it.unical.dimes.service.UserService;
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private UserService userService;

    public UserServiceImpl(UserService userService){
        this.userService = userService;
    }

    @Override
    public void login(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();

        if(username==null || username.trim().isEmpty()){
            System.out.println("login failed");
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Username vuoto")
                    .asRuntimeException());
            return;
        }

        try {
            User user;

            //cerco nel db
            User existingUser = userService.findByUsername(username);

            if (existingUser != null) {
                user = existingUser;
            } else {
                User newUser = new User(username);
                user = userService.save(newUser);
            }

            UserResponse response = UserResponse.newBuilder()
                    .setUserId(user.getId())
                    .setUsername(user.getUsername())
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(Status.INTERNAL.withDescription("Errore interno server").asRuntimeException());
        }
    }
}
