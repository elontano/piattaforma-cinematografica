package it.unical.dimes.view;

import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.UserAuthentication;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

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

        //Header
        Text title = new Text("Benvenuto");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Text subtitle = new Text("Accedi o crea un nuovo account");
        subtitle.setFill(Color.GRAY);

        VBox header = new VBox(5, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        //Input
        usernameField = uiFactory.createTextField("Username", 250);
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(250);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Bottoni
        // Usiamo la factory, ma per il Register e Login usiamo pulsanti standard per ora
        // o adattiamo la factory se hai aggiunto tipi specifici.
        Button btnLogin = new Button("Login");
        btnLogin.setDefaultButton(true); // Si attiva premendo ENTER
        btnLogin.getStyleClass().addAll("button", "success"); // Stile AtlantaFX (verde)

        Button btnRegister = new Button("Registrati");
        btnRegister.getStyleClass().addAll("button", "accent"); // Stile AtlantaFX (blu)

        Button btnCancel = new Button("Annulla");

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

        //Logica Eventi
        btnLogin.setOnAction(e -> {
            isRegister = false; //login
            isConfirmed = true;
            stage.close();
        });

        btnRegister.setOnAction(e -> {
            isRegister = true; //registrazione
            isConfirmed = true;
            stage.close();
        });

        btnCancel.setOnAction(e -> {
            isConfirmed = false;
            stage.close();
        });

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

    /**
     * Mostra la finestra e attende la chiusura.
     * Restituisce Optional con User/Pass se confermato, vuoto altrimenti.
     */
    public Optional<UserAuthentication> showAndWait() {
        stage.showAndWait();

        if (isConfirmed) {
            return Optional.of(new UserAuthentication(usernameField.getText(), passwordField.getText(),isRegister));
        } else {
            return Optional.empty();
        }
    }

}
