package it.unical.dimes.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public interface UIFactory {

    Button createButton(String text, ButtonType type, EventHandler<ActionEvent> action);

    TextField createTextField(String prompt, double width);

    ToggleButton createToggleButton(String text, double width);

    <T> ComboBox<T> createComboBox(String prompt, T... items);

    default TextField createTextField(String prompt) {
        return createTextField(prompt, 0);
    }
}
