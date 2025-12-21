package it.unical.dimes.view;

import it.unical.dimes.factory.ButtonType;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.FilmFilter;
import it.unical.dimes.model.SortBy;
import it.unical.dimes.model.ViewingStatus;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

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

    private void createFields(){
        titleField = uiFactory.createTextField("Title...",130);
        directorField = uiFactory.createTextField("Director...",130);
        genreField = uiFactory.createTextField("Genre...",100);
        yearField = uiFactory.createTextField("Year...",80);

        //Provvisorio per NON vedere UNKNOWN STATUS nella toolbar
        ViewingStatus[] vs = ViewingStatus.values();
        ViewingStatus[] newVS = new ViewingStatus[vs.length-1];
        for(int i=1;i<vs.length;i++){
            newVS[i-1]=vs[i];
        }

        statusComboBox = uiFactory.createComboBox("Status",newVS);
        statusComboBox.setPrefWidth(100);
        sortByComboBox = uiFactory.createComboBox("Sort by",SortBy.values());

        sortDirectionToggle = uiFactory.createToggleButton("⬆",70);
        sortDirectionToggle.setSelected(true);

        sortDirectionToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(sortDirectionToggle.isSelected()){
                    sortDirectionToggle.setText("⬆");
                }else {
                    sortDirectionToggle.setText("⬇");
                }
            }
        });
    }

    private void createButtons(){

        searchButton = uiFactory.createButton("🔍Search ", ButtonType.SEARCH, e->{
            if(onSearchAction!=null) {
                onSearchAction.accept(buildFilterFromFields());
                System.out.println(buildFilterFromFields());
            }
        });

        addButton = uiFactory.createButton("+New",ButtonType.NEW,e->{
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
        if (selectedStatus == null) selectedStatus = ViewingStatus.TO_WATCH; // Valore di default

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
