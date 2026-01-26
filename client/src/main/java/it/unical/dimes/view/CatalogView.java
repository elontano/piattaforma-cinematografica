package it.unical.dimes.view;

import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.Film;
import it.unical.dimes.model.FilmFilter;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

public class CatalogView {
    private BorderPane root;
    private FilmTable filmTable;
    private final UIFactory uiFactory;

    // Event Handlers
    private Consumer<Film> onEditAction;
    private Consumer<Film> onDeleteAction;
    private Consumer<FilmFilter> onSearchAction;
    private Runnable onAddAction;
    private Runnable onRefreshAction;

    public CatalogView(UIFactory factory) {
        this.uiFactory = factory;
        initView();
    }

    public UIFactory getUiFactory(){
        return uiFactory;
    }

    private void initView() {
        root = new BorderPane();
        root.setPadding(new Insets(15));

        AppToolBar toolBar = new AppToolBar(uiFactory);
        filmTable = new FilmTable(uiFactory);

        //collegamento eventi
        //FilmView qua prende l'evento dal componente figlio e lo passa al Controller

        toolBar.setOnSearchAction(filter -> {
            if(onSearchAction != null)
                onSearchAction.accept(filter);
        });

        toolBar.setOnAddAction(()->{
            if(onAddAction!=null)
                onAddAction.run();
        });

        toolBar.setOnRefreshAction(()->{
            if(onRefreshAction!=null)
                onRefreshAction.run();
        });

        filmTable.setOnEditAction(film -> {
            if(onEditAction != null)
                onEditAction.accept(film);
        });

        filmTable.setOnDeleteAction(film -> {
            if(onDeleteAction!=null)
                onDeleteAction.accept(film);
        });

        root.setTop(toolBar.getView());
        root.setCenter(filmTable.getView());
    }


    // Restituisce la vista da passare alla Scene
    public Parent getView() {
        return root;
    }

    public void setFilmData(ObservableList<Film> films){
        filmTable.setItems(films);
    }

    public FilmTable getFilmTable() {
        return filmTable;
    }

    public ObservableList<Film> getFilmsList(){
        return filmTable.getItems();
    }

    public void refreshTable(){
        filmTable.refresh();
    }

    // Setters per i Consumer (chiamati dal Controller)
    public void setOnSearchAction(Consumer<FilmFilter> action) { this.onSearchAction = action; }
    public void setOnAddAction(Runnable action) { this.onAddAction = action; }
    public void setOnEditAction(Consumer<Film> action) { this.onEditAction = action; }
    public void setOnDeleteAction(Consumer<Film> action) { this.onDeleteAction = action; }
    public void setOnRefreshAction(Runnable action) { this.onRefreshAction = action; }
}
