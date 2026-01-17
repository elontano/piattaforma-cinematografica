package it.unical.dimes.controller;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.dimes.entities.User;
import it.unical.dimes.exception.UserAlreadyExistsException;
import it.unical.dimes.protocol.UserRequest;
import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.protocol.UserServiceGrpc;
import it.unical.dimes.services.UserService;

import java.util.logging.Logger;

public class UserGrpcController extends UserServiceGrpc.UserServiceImplBase {
    private static final Logger logger = Logger.getLogger(UserGrpcController.class.getName());

    private final UserService userService;

    public UserGrpcController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void register(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        String plainPassword = request.getPassword();

        try {
            User registeredUser = userService.registerUser(new User(username, plainPassword));

            UserResponse response = UserResponse.newBuilder()
                    .setUserId(registeredUser.getId())
                    .setUsername(registeredUser.getUsername())
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (UserAlreadyExistsException e) {
            logger.warning("Tentativo registrazione utente già esistente: " + e.getMessage());
            responseObserver.onError(Status.ALREADY_EXISTS
                    .withDescription("Username già esistente")
                    .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Errore registrazione")
                    .asRuntimeException());
            logger.severe("Error during registration: " + e.getMessage());
        }
    }

    @Override
    public void login(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        String candidatePassword = request.getPassword();

        try {
            User user = userService.findByUsername(username);
            boolean loginSuccess = false;

            if(user!=null){
                loginSuccess = userService.checkPassword(candidatePassword,user.getPassword());
            }

            if(!loginSuccess){
                UserResponse response = UserResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Utente o Password errati")
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            //successo
            UserResponse response = UserResponse.newBuilder()
                    .setUserId(user.getId())
                    .setUsername(user.getUsername())
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Errore login")
                    .asRuntimeException());
            logger.severe("Error during login: " + e.getMessage());
        }
    }
}
