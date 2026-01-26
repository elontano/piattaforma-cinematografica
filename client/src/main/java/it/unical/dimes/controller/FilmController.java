package it.unical.dimes.controller;

import it.unical.dimes.command.DeleteCommand;
import it.unical.dimes.command.SaveCommand;
import it.unical.dimes.command.SearchCommand;
import it.unical.dimes.model.Film;
import it.unical.dimes.model.FilmFilter;
import it.unical.dimes.service.FilmServiceClient;
import it.unical.dimes.view.FilmFormDialog;
import it.unical.dimes.view.CatalogView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class FilmController {
    private final CatalogView view;
    private final FilmServiceClient client;
    private final int userId;

    public FilmController(CatalogView view, FilmServiceClient client, int userId) {
        this.view = view;
        this.client = client;
        this.userId = userId;
        bindActions();

        refreshData();
    }

    private void bindActions() {
        view.setOnSearchAction(this::handleSearch);
        view.setOnAddAction(this::handleAdd);
        view.setOnEditAction(this::handleEdit);
        view.setOnDeleteAction(this::handleDelete);
        view.setOnRefreshAction(this::refreshData);
    }

    private void handleSearch(FilmFilter filter) {

        new SearchCommand(view, client, filter,userId).execute();
    }

    private void handleAdd() {
        FilmFormDialog dialog = new FilmFormDialog(null,view.getUiFactory());

        dialog.showAndWait().ifPresent(newFilm -> {
            new SaveCommand(client, newFilm, userId, this::refreshData).execute();
        });
    }

    private void handleEdit(Film film) {
        FilmFormDialog dialog = new FilmFormDialog(film,view.getUiFactory());
        dialog.showAndWait().ifPresent(updatedFilm -> {
            Runnable onSuccess = () -> {
                int index = view.getFilmsList().indexOf(film);
                if(index>=0)
                    view.getFilmsList().set(index,updatedFilm);
                view.refreshTable();
            };
            new SaveCommand(client, updatedFilm, userId, onSuccess).execute();
        });
    }

    private void handleDelete(Film film) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove '" + film.getTitle() + "' from your collection ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new DeleteCommand(client, film.getId(), userId,this::refreshData).execute();
            }
        });
    }

    private void refreshData() {
        handleSearch(new FilmFilter.Builder().build());
    }
}
