package it.unical.dimes.view;

import it.unical.dimes.factory.ButtonType;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.UserAuthentication;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginDialog {

    //form
    private final Stage stage;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final Label confirmPasswordLabel;

    private final Button btnAction;
    private final Hyperlink registerLink;
    private final Node headerLogin;
    private final Node headerRegister;
    private final VBox headerContainer;


    private boolean isConfirmed = false;
    private boolean isRegisterMode = false;

    public LoginDialog(UIFactory uiFactory) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Blocca le altre finestre
        stage.setResizable(false);

        headerLogin = uiFactory.createHeader("Welcome", "Log in to continue!");
        headerRegister = uiFactory.createHeader("Create new Account", "Enter your details to register,the password must contain at least 8 characters.");
        headerContainer = new VBox(headerLogin);
        headerContainer.setAlignment(Pos.CENTER);

        usernameField = uiFactory.createTextField("Username", 250);
        passwordField = uiFactory.createPasswordField("Password (min 8 car.)", 250);
        confirmPasswordField = uiFactory.createPasswordField("Confirm password", 250);
        confirmPasswordLabel = new Label("Confirm");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);

        btnAction = uiFactory.createButton("Sign In", ButtonType.LOGIN, e -> {
            isConfirmed = true;
            stage.close();
        });

        Button btnCancel = uiFactory.createButton("Cancel", ButtonType.CANCEL, e -> {
            isConfirmed = false;
            stage.close();
        });

        HBox buttonBox = new HBox(10, btnAction, btnCancel);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        registerLink = new Hyperlink("Don't have an account? Register here.");
        registerLink.setStyle("-fx-font-size: 12px; -fx-underline: true;");
        registerLink.setTextAlignment(TextAlignment.CENTER);
        registerLink.setOnAction(e -> toggleMode());

        VBox switchBox = new VBox(registerLink);
        switchBox.setAlignment(Pos.CENTER);
        switchBox.setPadding(new Insets(10, 0, 0, 0));

        // Layout Principale
        VBox root = new VBox(10, headerContainer, grid, buttonBox, switchBox);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: -color-bg-default;"); // Sfondo del tema

        Scene scene = new Scene(root);
        stage.setScene(scene);

        updateUI();

        setupValidation();

        // Focus iniziale
        Platform.runLater(usernameField::requestFocus);
    }

    private void toggleMode() {
        isRegisterMode = !isRegisterMode;
        updateUI();
        setupValidation();
    }

    private void updateUI() {
        passwordField.clear();
        confirmPasswordField.clear();

        if (isRegisterMode) {
            stage.setTitle("New Account");
            headerContainer.getChildren().setAll(headerRegister);

            confirmPasswordLabel.setVisible(true);
            confirmPasswordLabel.setManaged(true);
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);

            btnAction.setText("Sign UP!");

            registerLink.setText("Do you already have an account? Log in");
        } else {
            stage.setTitle("Login Library");
            headerContainer.getChildren().setAll(headerLogin);
            confirmPasswordLabel.setVisible(false);
            confirmPasswordLabel.setManaged(false);
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);

            btnAction.setText("Sign In!");

            registerLink.setText("Don't have an account? Register here.");
        }

        stage.sizeToScene();
    }

    private void setupValidation() {
        Runnable validate = () -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();
            String confirm = confirmPasswordField.getText();

            passwordField.setStyle("");
            confirmPasswordField.setStyle("");

            boolean invalid;

            if (isRegisterMode) {
                boolean lenghtOk = password.length()>=8;
                boolean matchOk = password.equals(confirm);

                if (!password.isEmpty()) {
                    if (!lenghtOk) {
                        passwordField.setStyle("-fx-border-color: red;");
                    } else if (!matchOk && !confirm.isEmpty()) {
                        confirmPasswordField.setStyle("-fx-border-color: red;");
                    } else if (lenghtOk && matchOk) {
                        passwordField.setStyle("-fx-border-color:#10B981 ");
                        confirmPasswordField.setStyle("-fx-border-color:#10B981");
                    }
                }
                invalid = username.isEmpty() || !lenghtOk || !matchOk;
            }else {
                invalid = username.isEmpty() || password.isEmpty();
            }
            btnAction.setDisable(invalid);
        };

            usernameField.textProperty().addListener((o, old, val) -> validate.run());
            passwordField.textProperty().addListener((o, old, val) -> validate.run());
            confirmPasswordField.textProperty().addListener((o, old, val) -> validate.run());

            validate.run();
        }

        public Optional<UserAuthentication> showAndWait () {
            stage.showAndWait();

            if (isConfirmed) {
                return Optional.of(new UserAuthentication(usernameField.getText(), passwordField.getText(), isRegisterMode));
            } else {
                return Optional.empty();
            }
        }
}
