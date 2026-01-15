package it.unical.dimes.factory;

import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
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
                // button.getStyleClass().add(Styles.ACCENT);
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

            case SAVE: // Assicurati di avere SAVE in ButtonType
                button.setGraphic(new FontIcon(FontAwesomeSolid.SAVE));
                button.getStyleClass().add(Styles.SUCCESS);
                break;

            case CANCEL: // Se lo hai
                // Nessun colore particolare, magari solo icona
                button.setGraphic(new FontIcon(FontAwesomeSolid.TIMES));
                break;

            default:
                // Stile standard, nessuna icona specifica
                break;
        }

        // Impostiamo un po' di spazio tra icona e testo
        button.setGraphicTextGap(8);

        return button;
    }

    @Override
    public TextField createTextField(String prompt, double width) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
//        tf.getStyleClass().add("text-field-custom");

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

}
