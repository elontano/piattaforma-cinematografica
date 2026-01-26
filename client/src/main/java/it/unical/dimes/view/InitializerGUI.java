package it.unical.dimes.view;

import atlantafx.base.theme.PrimerLight;
import it.unical.dimes.command.Command;
import it.unical.dimes.command.SignInCommand;
import it.unical.dimes.command.SignUpCommand;
import it.unical.dimes.controller.FilmController;
import it.unical.dimes.factory.StandardUIFactory;
import it.unical.dimes.factory.UIFactory;
import it.unical.dimes.model.UserAuthentication;
import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.service.FilmServiceClient;
import it.unical.dimes.service.UserServiceClient;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class InitializerGUI extends Application {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 50051;

    private Stage primaryStage;
    private UIFactory uiFactory;
    private UserServiceClient userServiceClient;

    @Override
    public void start(Stage primaryStage) {

        Platform.setImplicitExit(false);

        this.primaryStage = primaryStage;
        this.uiFactory = new StandardUIFactory();

        this.userServiceClient = new UserServiceClient(HOST,PORT);

        //tema di AtlantaFX primer light e usando setUserAgenStyleSheet il tema si applica anche ai vari dialogs
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        showAuthenticationDialog();
    }

    private void showAuthenticationDialog(){
        LoginDialog loginDialog = new LoginDialog(uiFactory);

        loginDialog.showAndWait().ifPresentOrElse(
                authData -> handleAuthenticationRequest(authData),
                //se l'utente ha chiuso o annullato
                () -> {
                    userServiceClient.shutdown();
                    Platform.exit();
                }
        );
    }

    private void handleAuthenticationRequest(UserAuthentication user){
        Consumer<UserResponse> onResult = (response)->{
            if(response!=null && response.getSuccess()){
                initApplication(response);
            }else{
                String errorTitle = user.isRegister() ? "Registration Error " : "Login Error";

                String errorMessage = (response != null && response.getMessage() != null)
                        ? response.getMessage()
                        : "Errore sconosciuto di comunicazione.";

                showError(errorTitle, errorMessage);

                showAuthenticationDialog();
            }
        };

        Command command;
        if(user.isRegister()){
            command = new SignUpCommand(userServiceClient,user.username(),user.password(),onResult);
        }else {
            command = new SignInCommand(userServiceClient,user.username(),user.password(),onResult);
        }

        command.execute();
    }

    private void initApplication(UserResponse user){
        userServiceClient.shutdown();

        //Configurazione dopo login
        CatalogView filmView = new CatalogView(uiFactory);
        FilmServiceClient filmServiceClient = new FilmServiceClient(HOST, PORT);

        FilmController controller = new FilmController(filmView, filmServiceClient, user.getUserId());

        Scene scene = new Scene(filmView.getView(), 1000, 600);
        primaryStage.setTitle("Video Library of: " + user.getUsername());
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        primaryStage.show();
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
