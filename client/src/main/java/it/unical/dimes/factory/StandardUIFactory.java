package it.unical.dimes.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class StandardUIFactory implements UIFactory {

    @Override
    public Button createButton(String text, ButtonType type, EventHandler<ActionEvent> action) {
        Button btn = new Button(text);

        switch (type) {
            case SEARCH -> btn.getStyleClass().add("btn-search");
            case NEW    -> btn.getStyleClass().add("btn-new");
            case DELETE -> btn.getStyleClass().add("btn-delete");
            case EDIT   -> btn.getStyleClass().add("btn-edit");
        }

        if (action != null) btn.setOnAction(action);
        return btn;
    }

    @Override
    public TextField createTextField(String prompt, double width) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("text-field-custom");

        if(width>0)
            tf.setPrefWidth(width);
        return tf;
    }

    @Override
    public ToggleButton createToggleButton(String text, double width) {
        ToggleButton tb = new ToggleButton(text);
        tb.setPrefWidth(width);
        return tb;
    }

    @Override
    public <T> ComboBox<T> createComboBox(String prompt, T... items) {
        ComboBox<T> comboBox = new ComboBox<>();
        comboBox.getItems().setAll(items);
        comboBox.setPromptText(prompt);
        comboBox.setTooltip(new Tooltip(prompt));
        return comboBox;
    }

}
