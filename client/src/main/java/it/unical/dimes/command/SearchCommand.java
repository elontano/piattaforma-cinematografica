package it.unical.dimes.command;

import it.unical.dimes.model.Film;
import it.unical.dimes.model.FilmFilter;
import it.unical.dimes.service.FilmServiceClient;
import it.unical.dimes.view.CatalogView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SearchCommand implements Command {

    private CatalogView view;
    private FilmServiceClient client;
    private FilmFilter filter;
    private Integer userId;

    public SearchCommand(CatalogView view, FilmServiceClient client, FilmFilter filter, Integer userId) {
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

                //ObservableList -> lista di JavaFX, osserva i cambiamenti e notifica chi la sta cambiando
                ObservableList<Film> observableList = FXCollections.observableArrayList(results);

                Platform.runLater(() -> {
                    view.setFilmData(observableList);
                });

            } catch (Exception e) {
                Platform.runLater(() -> System.err.println("Error search: " + e.getMessage()));
            }
        }).start();
    }
}
