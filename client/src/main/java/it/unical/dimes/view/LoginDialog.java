package it.unical.dimes.view;

import it.unical.dimes.factory.ButtonType;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.UserAuthentication;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginDialog {

    private final Stage stage;
    private final TextField usernameField;
    private final PasswordField passwordField;

    private boolean isConfirmed = false;
    private boolean isRegister = false;

    public LoginDialog(UIFactory uiFactory){
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Blocca le altre finestre
        stage.setTitle("Accesso Videoteca");
        stage.setResizable(false);

        Node header = uiFactory.createHeader("Welcome", "Log in or create a new account!");

        usernameField = uiFactory.createTextField("Username", 250);
        passwordField = uiFactory.createPasswordField("Password",250);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        Button btnLogin = uiFactory.createButton("Sign In", ButtonType.LOGIN,e->{
            isRegister = false; //login
            isConfirmed = true;
            stage.close();
        });

        Button btnRegister = uiFactory.createButton("Sign Up",ButtonType.REGISTER,e->{
            isRegister = true;
            isConfirmed = true;
            stage.close();
        });

        Button btnCancel = uiFactory.createButton("Annulla", ButtonType.CANCEL, e ->{
            isConfirmed=false;
            stage.close();
        });

        HBox buttonBox = new HBox(10, btnLogin, btnRegister, btnCancel);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        // Layout Principale
        VBox root = new VBox(10, header, grid, buttonBox);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: -color-bg-default;"); // Sfondo del tema

        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Validazione: Disabilita Login/Register se campi vuoti
        Runnable validate = () -> {
            boolean invalid = usernameField.getText().trim().isEmpty() || passwordField.getText().isEmpty();
            btnLogin.setDisable(invalid);
            btnRegister.setDisable(invalid);
        };

        usernameField.textProperty().addListener((o, old, val) -> validate.run());
        passwordField.textProperty().addListener((o, old, val) -> validate.run());
        validate.run(); // Check iniziale

        // Focus iniziale
        Platform.runLater(usernameField::requestFocus);
    }

    public Optional<UserAuthentication> showAndWait() {
        stage.showAndWait();

        if (isConfirmed) {
            return Optional.of(new UserAuthentication(usernameField.getText(), passwordField.getText(),isRegister));
        } else {
            return Optional.empty();
        }
    }
}
