package it.unical.dimes.command;

import it.unical.dimes.model.Film;
import it.unical.dimes.model.FilmFilter;
import it.unical.dimes.service.FilmServiceClient;
import it.unical.dimes.view.FilmView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SearchCommand implements Command {

    private FilmView view;
    private FilmServiceClient client;
    private FilmFilter filter;
    private String userId;

    public SearchCommand(FilmView view, FilmServiceClient client, FilmFilter filter, String userId) {
        this.view = view;
        this.client = client;
        this.filter = filter;
        this.userId = userId;
    }

    @Override
    public void execute() {
        new Thread(() -> {
            try {
                List<Film> results = client.searchFilms(filter,userId);

                ObservableList<Film> observableList = FXCollections.observableArrayList(results);

                Platform.runLater(() -> {
                    view.setFilmData(observableList); // Chiama il metodo della tua View
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> System.err.println("Errore ricerca: " + e.getMessage()));
            }
        }).start();
    }
}
