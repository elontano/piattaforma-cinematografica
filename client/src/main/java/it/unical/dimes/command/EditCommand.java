package it.unical.dimes.command;

import it.unical.dimes.model.Film;
import it.unical.dimes.service.FilmServiceClient;
import javafx.application.Platform;

public class EditCommand implements Command{

    private final FilmServiceClient client;
    private final Film film;
    private final Runnable onSuccessCallback;
    private final String userId;

    public EditCommand(FilmServiceClient client, Film film,String userId, Runnable onSuccessCallback) {
        this.client = client;
        this.film = film;
        this.onSuccessCallback = onSuccessCallback;
        this.userId=userId;
    }

    @Override
    public void execute() {
        new Thread(() -> {
            try {
                client.update(film,userId);

                Platform.runLater(() -> {
                    if (onSuccessCallback != null) {
                        onSuccessCallback.run();
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> System.err.println("Errore durante il salvataggio: " + e.getMessage()));
            }
        }).start();
    }
}
