package it.unical.dimes.command;

import it.unical.dimes.model.Film;
import it.unical.dimes.service.FilmServiceClient;
import javafx.application.Platform;

public class SaveCommand implements Command{

    private final FilmServiceClient client;
    private final Film film;
    private final Integer userId;
    private final Runnable onSuccess; // Cosa fare dopo aver salvato (es. refresh)

    public SaveCommand(FilmServiceClient client, Film film, Integer userId, Runnable onSuccess) {
        this.client = client;
        this.film = film;
        this.onSuccess = onSuccess;
        this.userId = userId;
    }

    @Override
    public void execute() {
        new Thread(() -> {
            try {
                if (film.getId() == 0) {
                    client.createFilm(film,userId);
                } else {
                    client.update(film,userId);
                }

                Platform.runLater(() -> {
                    if (onSuccess != null)
                        onSuccess.run();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    System.err.println("Errore: " + e.getMessage());
                });
            }
        }).start();
    }
}
