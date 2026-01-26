package it.unical.dimes.command;

import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.service.UserServiceClient;
import javafx.application.Platform;

import java.util.function.Consumer;

public class SignInCommand implements Command{
    private final UserServiceClient client;
    private final String username;
    private final String password;
    private final Consumer<UserResponse> onResult; // Callback per la risposta

    public SignInCommand(UserServiceClient client, String username, String password, Consumer<UserResponse> onResult) {
        this.client = client;
        this.username = username;
        this.password = password;
        this.onResult = onResult;
    }

    @Override
    public void execute() {
        //Thread separato per non bloccare l'UI
        new Thread(() -> {
            // Chiamata sincrona al server
            UserResponse response = client.login(username, password);

            // Ritorno al thread JavaFX per aggiornare l'interfaccia
            Platform.runLater(() -> {
                if (onResult != null) onResult.accept(response);
            });
        }).start();
    }}
