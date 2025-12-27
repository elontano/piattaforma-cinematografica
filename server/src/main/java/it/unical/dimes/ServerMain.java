package it.unical.dimes;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.unical.dimes.controller.CatalogServiceImpl;
import it.unical.dimes.controller.UserServiceImpl;
import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.User;
import it.unical.dimes.repositories.*;
import it.unical.dimes.service.FilmService;
import it.unical.dimes.service.UserService;

import java.io.IOException;

public class ServerMain {

    public static void avvia() throws IOException, InterruptedException {

        DBManager dbManager = DBManager.getInstance();
        dbManager.initDB();

        UserRepository userRepository = new UserRepositoryImpl(dbManager);
        UserService userService = new UserService(userRepository);
        UserServiceImpl userController = new UserServiceImpl(userService);

        FilmRepository filmRepository = new FilmRepositoryImpl(dbManager);
        FilmService filmService = new FilmService(filmRepository);
        CatalogServiceImpl controller = new CatalogServiceImpl(filmService);

        int port = 50051;
        Server server = ServerBuilder.forPort(port).addService(userController)
                .addService(controller)
                .build();
        server.start();

        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        avvia();
    }
}
