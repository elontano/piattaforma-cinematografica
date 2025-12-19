package it.unical.dimes.command;

import it.unical.dimes.service.FilmServiceClient;
import javafx.application.Platform;

public class DeleteCommand implements Command{

    private final FilmServiceClient client;
    private final int filmId;
    private final String userId;
    private final Runnable onSuccess;

    public DeleteCommand(FilmServiceClient client, int filmId, String userId, Runnable onSuccess) {
        this.client = client;
        this.filmId = filmId;
        this.onSuccess = onSuccess;
        this.userId = userId;
    }

    @Override
    public void execute() {
        new Thread(() -> {
            try {
                client.delete(filmId,userId);

                Platform.runLater(() -> {
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> System.err.println("Errore durante l'eliminazione: " + e.getMessage()));
            }
        }).start();
    }
}
