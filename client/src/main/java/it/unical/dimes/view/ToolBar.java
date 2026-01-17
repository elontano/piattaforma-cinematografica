package it.unical.dimes.view;

import atlantafx.base.theme.Styles;
import it.unical.dimes.factory.ButtonType;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.FilmFilter;
import it.unical.dimes.model.SortBy;
import it.unical.dimes.model.ViewingStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Consumer;

public class ToolBar {

    private final HBox toolBar;
    private final UIFactory uiFactory;

    //Filtri di Input per la Ricerca
    private TextField titleField;
    private TextField directorField;
    private TextField genreField;
    private TextField yearField;

    //    private Spinner<Integer> yearSpinner; //campo con frecce su e giù
    private ComboBox<ViewingStatus> statusComboBox;
    private ComboBox<SortBy> sortByComboBox;
    private ToggleButton sortDirectionToggle;

    private Button searchButton;
    private Button addButton;
    // private Button refreshButton; per ricaricare senza filtri

    // Event Handlers
    private Consumer<FilmFilter> onSearchAction;
    private Runnable onAddAction;

    public ToolBar(UIFactory factory){
        this.toolBar = new HBox(5);
        this.uiFactory = factory;

        initLayout();
    }

    private void initLayout(){
        toolBar.setPadding(new Insets(10));
        toolBar.setAlignment(Pos.CENTER);

        createFields();
        createButtons();

        // Spazio flessibile
        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        // Aggiungo tutto in una volta sola per chiarezza
        toolBar.getChildren().addAll(
                titleField, directorField, genreField,
                yearField, statusComboBox,
                sortByComboBox, sortDirectionToggle,
                searchButton, space, addButton
        );
    }

    private void createFields() {
        titleField = uiFactory.createTextField("Title...", 130);
        directorField = uiFactory.createTextField("Director...", 130);
        genreField = uiFactory.createTextField("Genre...", 100);
        yearField = uiFactory.createTextField("Year...", 80);

        // Gestione status per evitare UNKNOWN
        ViewingStatus[] vs = ViewingStatus.values();
        ViewingStatus[] newVS = new ViewingStatus[vs.length - 1];
        System.arraycopy(vs, 1, newVS, 0, vs.length - 1);

        statusComboBox = uiFactory.createComboBox("Status", newVS);
        statusComboBox.setPrefWidth(100);

        sortByComboBox = uiFactory.createComboBox("Sort by", SortBy.values());

        // Creiamo il toggle button tramite factory (testo vuoto perché usiamo l'icona)
        sortDirectionToggle = uiFactory.createToggleButton("", 40);

        // Stile: lo rendiamo un pulsante icona (quadrato/compatto)
        sortDirectionToggle.getStyleClass().add(Styles.BUTTON_ICON);

        // Icona iniziale (es. A-Z, ordinamento discendente standard)
        sortDirectionToggle.setGraphic(new FontIcon(FontAwesomeSolid.SORT_ALPHA_DOWN));
        sortDirectionToggle.setTooltip(new Tooltip("Ordinamento Crescente (A-Z)"));
        sortDirectionToggle.setSelected(false); // Default: non selezionato

        // Logica al click: cambio icona
        sortDirectionToggle.setOnAction(e -> {
            if (sortDirectionToggle.isSelected()) {
                // Se selezionato -> Z-A (Decrescente)
                sortDirectionToggle.setGraphic(new FontIcon(FontAwesomeSolid.SORT_ALPHA_UP));
                sortDirectionToggle.setTooltip(new Tooltip("Ordinamento Decrescente (Z-A)"));
            } else {
                // Se non selezionato -> A-Z (Crescente)
                sortDirectionToggle.setGraphic(new FontIcon(FontAwesomeSolid.SORT_ALPHA_DOWN));
                sortDirectionToggle.setTooltip(new Tooltip("Ordinamento Crescente (A-Z)"));
            }
        });
    }

    private void createButtons(){
        searchButton = uiFactory.createButton("Cerca", ButtonType.SEARCH, e -> {
            if(onSearchAction != null) {
                onSearchAction.accept(buildFilterFromFields());
            }
        });

        addButton = uiFactory.createButton("Nuovo", ButtonType.NEW, e -> {
            if(onAddAction != null)
                onAddAction.run();
        });
    }

    private FilmFilter buildFilterFromFields() {
        Integer year = null;
        try {
            String yearText = yearField.getText().trim();
            if (!yearText.isEmpty()) {
                year = Integer.parseInt(yearText);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid year, year filter ignored.");
        }

        ViewingStatus selectedStatus = statusComboBox.getValue();

        SortBy selectedSort = sortByComboBox.getValue();
        if (selectedSort == null) selectedSort = SortBy.NONE; // Valore di default

        FilmFilter.Builder filterBuilder = new FilmFilter.Builder()
                .title(titleField.getText().trim())
                .director(directorField.getText().trim())
                .genre(genreField.getText().trim())
                .yearOfRelease(year)
                .viewingStatus(selectedStatus)
                .sortBy(selectedSort)
                .sortDirection(sortDirectionToggle.isSelected());

        return filterBuilder.build();
    }

    public Node getView(){
        return toolBar;
    }

    public void setOnSearchAction(Consumer<FilmFilter> onSearchAction) { this.onSearchAction = onSearchAction; }
    public void setOnAddAction(Runnable onAddAction) { this.onAddAction = onAddAction; }
}
