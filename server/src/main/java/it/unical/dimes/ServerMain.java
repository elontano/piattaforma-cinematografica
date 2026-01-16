package it.unical.dimes;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.unical.dimes.controller.CatalogGrpcController;
import it.unical.dimes.controller.UserGrpcController;
import it.unical.dimes.repositories.*;
import it.unical.dimes.services.FilmService;
import it.unical.dimes.services.UserService;

import java.io.IOException;

public class ServerMain {

    private static final int PORT = 500051;

    public static void initServer() throws IOException, InterruptedException {

        DBManager dbManager = DBManager.getInstance();
        dbManager.initDB();

        UserRepository userRepository = new UserRepositoryImpl(dbManager);
        UserService userService = new UserService(userRepository);
        UserGrpcController userController = new UserGrpcController(userService);

        FilmRepository filmRepository = new FilmRepositoryImpl(dbManager);
        FilmService filmService = new FilmService(filmRepository);
        CatalogGrpcController controller = new CatalogGrpcController(filmService);

        Server server = ServerBuilder.forPort(PORT)
                .addService(userController)
                .addService(controller)
                .build();
        server.start();

        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        initServer();
    }
}
