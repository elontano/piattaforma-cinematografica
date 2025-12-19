package it.unical.dimes.view;

import it.unical.dimes.controller.FilmController;
import it.unical.dimes.factory.StandardUIFactory;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.service.FilmServiceClient;
import javafx.application.Application;


import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class ClientMain extends Application {

    @Override
    public void start(Stage primaryStage) {

        String userID = getUserID();

        if(userID == null || userID.trim().isEmpty()){
            System.out.println("No user selected.");
            Platform.exit();
            return;
        }


        UIFactory factory  = new StandardUIFactory();
        FilmView filmView = new FilmView(factory);
        FilmServiceClient filmServiceClient = new FilmServiceClient("127.0.0.1",50051);

        FilmController controller = new FilmController(filmView,filmServiceClient,userID);

        Scene scene = new Scene(filmView.getView(), 900, 600);
        primaryStage.setTitle("Piattaforma cinematografica di: "+userID);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/standardStyle.css").toExternalForm());

        primaryStage.show();
    }

    private String getUserID(){
        TextInputDialog dialog = new TextInputDialog("user1"); // Valore di default per test rapidi
        dialog.setTitle("Login Videoteca");
        dialog.setHeaderText("Benvenuto nella Videoteca Distribuita");
        dialog.setContentText("Inserisci il tuo User ID per continuare:");

        // Disabilita il pulsante OK se il campo è vuoto (opzionale, tocco di classe)
        // Node loginButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        // loginButton.setDisable(true);
        // ... logica listener sul text field ...

        Optional<String> result = dialog.showAndWait();

        // Restituisce la stringa se premuto OK, altrimenti null
        return result.orElse(null);    }

    public static void main(String[] args) {
        launch(args);
    }
}
