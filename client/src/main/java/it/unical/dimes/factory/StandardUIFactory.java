package it.unical.dimes.factory;

import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class StandardUIFactory implements UIFactory {

    @Override
    public Button createButton(String text, ButtonType type, EventHandler<ActionEvent> handler) {
        Button button = new Button(text!=null ? text : "");

        // Impostiamo l'azione se presente
        if (handler != null) {
            button.setOnAction(handler);
        }

        // Configurazione Stile e Icona in base al TIPO
        switch (type) {
            case SEARCH:
                button.setGraphic(new FontIcon(FontAwesomeSolid.SEARCH));
                break;
            case NEW:
                button.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));
                button.getStyleClass().add(Styles.SUCCESS); // Colore Verde
                break;

            case EDIT:
                button.setGraphic(new FontIcon(FontAwesomeSolid.PENCIL_ALT));
                button.getStyleClass().add(Styles.ACCENT); // O altro colore distintivo
                break;

            case DELETE:
                button.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
                button.getStyleClass().add(Styles.DANGER); // Colore Rosso
                break;

            case SAVE:
                button.setGraphic(new FontIcon(FontAwesomeSolid.SAVE));
                button.getStyleClass().add(Styles.SUCCESS);
                break;

            case CANCEL:
                // Nessun colore particolare, magari solo icona
                button.setGraphic(new FontIcon(FontAwesomeSolid.TIMES));
                break;

            case LOGIN:
                button.setGraphic(new FontIcon(FontAwesomeSolid.SIGN_IN_ALT));
                button.getStyleClass().add(Styles.ACCENT);
                button.setDefaultButton(true);
                break;

            case REGISTER:
                button.setGraphic(new FontIcon(FontAwesomeSolid.USER_PLUS));
                button.getStyleClass().add(Styles.ACCENT);

            default:
                break;
        }

        //spazio tra icona e testo
        button.setGraphicTextGap(8);

        return button;
    }

    @Override
    public TextField createTextField(String prompt, double width) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);

        if(width>0)
            tf.setPrefWidth(width);
        return tf;
    }

    @Override
    public ToggleButton createToggleButton(String text, double width) {
        ToggleButton tb = new ToggleButton(text);

        if(width>0)
            tb.setPrefWidth(width);
        return tb;
    }

    @Override
    public <T> ComboBox<T> createComboBox(String prompt, T... items) {
        ComboBox<T> comboBox = new ComboBox<>();

        if(items != null){
            comboBox.getItems().setAll(items);
        }

        comboBox.setPromptText(prompt);
        comboBox.setTooltip(new Tooltip(prompt));
        return comboBox;
    }

    @Override
    public Node createHeader(String titleText, String subtitleText) {
        VBox box = new VBox(5); // Spaziatura verticale di 5px
        box.setAlignment(Pos.CENTER);

        // Configurazione Titolo
        if (titleText != null && !titleText.isEmpty()) {
            Label title = new Label(titleText);
            title.getStyleClass().add(Styles.TITLE_2);
            box.getChildren().add(title);
        }

        // Configurazione Sottotitolo
        if (subtitleText != null && !subtitleText.isEmpty()) {
            Label subtitle = new Label(subtitleText);
            subtitle.getStyleClass().add(Styles.TEXT_MUTED);
            box.getChildren().add(subtitle);
        }

        box.setPadding(new Insets(0, 0, 20, 0));

        return box;
    }

    @Override
    public PasswordField createPasswordField(String prompt, double width) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(prompt);
        passwordField.setPrefWidth(width);
        return passwordField;
    }

    public Spinner<Integer> createIntegerSpinner(int min, int max, int initial){
        Spinner<Integer> spinner = new Spinner<>(min,max,initial);
        spinner.setEditable(true);
        return spinner;
    }
}
