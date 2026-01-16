package it.unical.dimes.command;

import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.service.UserServiceClient;
import javafx.application.Platform;

import java.util.function.Consumer;

public class RegisterCommand implements Command {
    private final UserServiceClient client;
    private final String username;
    private final String password;
    private final Consumer<UserResponse> onResult;

    public RegisterCommand(UserServiceClient client, String username, String password, Consumer<UserResponse> onResult) {
        this.client = client;
        this.username = username;
        this.password = password;
        this.onResult = onResult;
    }

    @Override
    public void execute() {
        new Thread(() -> {
            UserResponse response = client.register(username, password);
            Platform.runLater(() -> {
                if (onResult != null) onResult.accept(response);
            });
        }).start();
    }
}
