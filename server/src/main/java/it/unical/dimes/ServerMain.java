package it.unical.dimes;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.unical.dimes.controller.CatalogServiceImpl;
import it.unical.dimes.repository.DBManager;
import it.unical.dimes.repository.FilmRepository;
import it.unical.dimes.repository.FilmRepositoryImpl;
import it.unical.dimes.service.FilmService;

import java.io.IOException;

public class ServerMain {

    public static void avvia() throws IOException, InterruptedException {

        DBManager.getInstance().initDB();
        FilmRepository filmRepository = new FilmRepositoryImpl(DBManager.getInstance());
        FilmService filmService = new FilmService(filmRepository);
        CatalogServiceImpl controller = new CatalogServiceImpl(filmService);

        int port = 50051;
        Server server = ServerBuilder.forPort(port).addService(controller).build();
        server.start();

        System.out.println("Server avviato su porta"+port);

        server.awaitTermination();
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        avvia();
    }
}
