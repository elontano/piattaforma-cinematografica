package it.unical.dimes.view;

import it.unical.dimes.model.Film;
import it.unical.dimes.model.ViewingStatus;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class FilmFormDialog {
    private final Dialog<Film> filmDialog;

    private final TextField titleField = new TextField();
    private final TextField directorField = new TextField();
    private final TextField genreField = new TextField();
    private final TextField yearField = new TextField();
    private final Spinner<Integer> ratingSpinner = new Spinner<>(0, 5, 0);
    private final ComboBox<ViewingStatus> statusComboBox = new ComboBox<>();

    private final Film existingFilm;

    public FilmFormDialog(Film filmToEdit) {
        filmDialog = new Dialog<>();
        this.existingFilm = filmToEdit;

        setTitleAndHeader(filmToEdit);

        ButtonType saveButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        filmDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        ratingSpinner.setEditable(true);

        GridPane grid = createGrid();
        filmDialog.getDialogPane().setContent(grid);

        statusComboBox.getItems().setAll(ViewingStatus.values());

        disableSaveButton(saveButtonType);

        if (existingFilm != null)
            populateFields();
        else
            statusComboBox.setValue(ViewingStatus.TO_WATCH);//di default

        filmDialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) return buildFilm();
            return null;
        });
    }

    private void setTitleAndHeader(Film filmToEdit){
        if(filmToEdit == null){
            filmDialog.setTitle("New Film");
            filmDialog.setHeaderText("Enter the details of the new film");
        }else {
            filmDialog.setTitle("Edit Film");
            filmDialog.setHeaderText("Edit movie details");
        }
    }

    private GridPane createGrid(){
        GridPane grid = new GridPane();
        grid.setHgap(10); //spazio orizzontale tra le colonne
        grid.setVgap(10); //spazio verticale tra le righe
        grid.setPadding(new Insets(20, 150, 10, 10)); //top,right,bottom,left quanto spazio vuoto lasciare attorno alla griglia

        grid.add(new Label("Title:"), 0, 0); grid.add(titleField, 1, 0);
        grid.add(new Label("Director:"), 0, 1); grid.add(directorField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2); grid.add(genreField, 1, 2);
        grid.add(new Label("Year:"), 0, 3); grid.add(yearField, 1, 3);
        grid.add(new Label("Rating:"), 0, 4); grid.add(ratingSpinner, 1, 4);
        grid.add(new Label("Status:"), 0, 5); grid.add(statusComboBox, 1, 5);

        return grid;
    }

    private void disableSaveButton(ButtonType saveButtonType){
        Node saveButton = filmDialog.getDialogPane().lookupButton(saveButtonType);

        //in caso di modifica del film, controllo se c'è già testo
        saveButton.setDisable(titleField.getText().trim().isEmpty());

        //changelistener per i cambiamenti
        titleField.textProperty().addListener(((observableValue, oldValue, newValue) ->{
            saveButton.setDisable(newValue.trim().isEmpty());
        } ));
    }

    private void populateFields() {
        titleField.setText(existingFilm.getTitle());
        directorField.setText(existingFilm.getDirector());
        genreField.setText(existingFilm.getGenre());

        int year = existingFilm.getYearOfRelease();
        yearField.setText(year > 0 ? String.valueOf(year) : "");

        Integer rating = existingFilm.getRating();
        ratingSpinner.getValueFactory().setValue(rating != null ? rating : 0);
        statusComboBox.setValue(existingFilm.getViewingStatus());
    }

    public Optional<Film> showAndWait() {
        return filmDialog.showAndWait();
    }

    private Film buildFilm() {
        Film.Builder film = new Film.Builder(titleField.getText());

        if (existingFilm != null)
            film.id(existingFilm.getId());
        else
            film.id(0);//forzo ID a 0
        film.director(directorField.getText()!=null?directorField.getText():"");
        film.genre(genreField.getText()!=null?genreField.getText():"");

        int year = 0;
        try {
            String yearText = yearField.getText().trim(); // Rimuove spazi
            if (!yearText.isEmpty()) {
                year = Integer.parseInt(yearText);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid year, set to 0");
            year = 0;
        }
        film.yearOfRelease(year);
        film.rating(ratingSpinner.getValue());
        film.viewingStatus(statusComboBox.getValue());
        return film.build();
    }
}
