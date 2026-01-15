package it.unical.dimes.view;
import it.unical.dimes.factory.ButtonType;
import it.unical.dimes.model.ViewingStatus;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.Film;

import java.util.function.Consumer;

public class FilmTable {
    private final TableView<Film> tableView;
    private final UIFactory uiFactory;

    // Event Handlers
    private Consumer<Film> onEditAction;
    private Consumer<Film> onDeleteAction;

    public FilmTable (UIFactory factory){
        this.uiFactory = factory;
        this.tableView = new TableView<>();

        //rende colonne larghe quanto basta per riempire la tabella
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setPlaceholder(new Label("Nessun film presente!"));

        initLayout();
    }

    private void initLayout(){
        createColumns();
    }

    private void createColumns(){
        // TITOLO
        TableColumn<Film, String> colTitle = new TableColumn<>("Title");
        colTitle.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTitle()));

        // REGISTA
        TableColumn<Film, String> colDirector = new TableColumn<>("Director");
        colDirector.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDirector()));

        // ANNO
        TableColumn<Film, String> colYear = new TableColumn<>("Year");
        colYear.setCellValueFactory(cell ->
                new SimpleStringProperty(String.valueOf(
                        (cell.getValue().getYearOfRelease()==0) ? "" : cell.getValue().getYearOfRelease()
                )));

        // RATING
        TableColumn<Film, Integer> colRating = new TableColumn<>("Rating");
        colRating.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getRating()));
        colRating.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null || rating == 0) {
                    setGraphic(null);
                    setText(null);
                } else {
                    String stars = "★".repeat(Math.max(0, rating)) + "☆".repeat(Math.max(0, 5 - rating));
                    Text text = new Text(stars);
                    text.setFill(Color.GOLD);
                    text.setStyle("-fx-font-size: 16px;");
                    setGraphic(text);
                    setAlignment(Pos.CENTER);
                }
            }
        });

        //STATUS
        TableColumn<Film, ViewingStatus> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getViewingStatus()));

        // Azioni Modifica / Elimina
        TableColumn<Film, Void> colAction = new TableColumn<>("Actions");
        colAction.setSortable(false);

        colAction.setCellFactory(param -> new TableCell<>() {
            // Usiamo la factory passando stringa vuota "" per avere solo le icone
            private final Button btnEdit = uiFactory.createButton("", ButtonType.EDIT, e -> {
                Film film = getTableView().getItems().get(getIndex());
                if (onEditAction != null) onEditAction.accept(film);
            });

            private final Button btnDelete = uiFactory.createButton("", ButtonType.DELETE, e -> {
                Film film = getTableView().getItems().get(getIndex());
                if (onDeleteAction != null) onDeleteAction.accept(film);
            });


            private final HBox pane = new HBox(10, btnEdit, btnDelete);

            {
                pane.setAlignment(Pos.CENTER);
                //aggiungere edit o delete in testo se necessario
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        tableView.getColumns().addAll(colTitle, colDirector, colYear, colRating, colStatus, colAction);
    }

    public Node getView(){
        return tableView;
    }

    // Metodo fondamentale per passare i dati alla tabella
    public void setItems(ObservableList<Film> films) {
        tableView.setItems(films);
    }

    public ObservableList<Film> getItems(){return tableView.getItems();}

    public void refresh(){
        tableView.refresh();
    }

    public void setOnEditAction(Consumer<Film> action) { this.onEditAction = action; }
    public void setOnDeleteAction(Consumer<Film> action) { this.onDeleteAction = action; }
}
