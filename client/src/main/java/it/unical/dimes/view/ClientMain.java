package it.unical.dimes.view;

import it.unical.dimes.controller.FilmController;
import it.unical.dimes.factory.StandardUIFactory;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.service.FilmServiceClient;
import it.unical.dimes.service.UserServiceClient;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class ClientMain extends Application {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 50051;

    @Override
    public void start(Stage primaryStage) {
        String username = getUsername();

        if(username == null || username.trim().isEmpty()){
            System.out.println("No user selected.");
            Platform.exit();
            return;
        }

        UserServiceClient userServiceClient = new UserServiceClient(HOST,PORT);

        UserResponse userResponse = userServiceClient.login(username);

        if(userResponse == null){
            showError("Login failed","Could not connect to server");
            Platform.exit();
            return;
        }

        int realUserID = userResponse.getUserId();
        String realUsername = userResponse.getUsername();

        userServiceClient.shutdown();

        UIFactory factory  = new StandardUIFactory();
        FilmView filmView = new FilmView(factory);
        FilmServiceClient filmServiceClient = new FilmServiceClient(HOST,PORT);

        FilmController controller = new FilmController(filmView,filmServiceClient,realUserID);

        Scene scene = new Scene(filmView.getView(), 900, 600);
        primaryStage.setTitle("Video Library of : "+realUsername);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/standardStyle.css").toExternalForm());

        primaryStage.show();
    }

    private String getUsername() {
        TextInputDialog dialog = new TextInputDialog(); // Valore di default per test rapidi

        dialog.setTitle("Login ");
        dialog.setHeaderText("Welcome to the Video Library");
        dialog.setContentText("Enter your Username to continue:");

        Node loginButton = dialog.getDialogPane().lookupButton(ButtonType.OK);

        loginButton.setDisable(true);

        dialog.getEditor().textProperty().addListener((observableValue, oldValue, newValue) ->
                loginButton.setDisable(newValue.trim().isEmpty()));

        Optional<String> result = dialog.showAndWait();
        System.out.println(result);

        return result.orElse(null);
    }

    private void showError(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
