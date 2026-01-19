package it.unical.dimes.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;

public interface UIFactory {

    Button createButton(String text, ButtonType type, EventHandler<ActionEvent> action);

    TextField createTextField(String prompt, double width);

    ToggleButton createToggleButton(String text, double width);

    <T> ComboBox<T> createComboBox(String prompt, T... items);

    Node createHeader(String titleText, String subTitleText);

    PasswordField createPasswordField(String prompt,double width);

    Spinner<Integer> createIntegerSpinner(int min, int max, int initial);

    default TextField createTextField(String prompt) {
        return createTextField(prompt, 0);
    }
}
