package it.unical.dimes;


import it.unical.dimes.entity.Film;
import it.unical.dimes.entity.FilmFilter;
import it.unical.dimes.entity.ViewingStatus;
import it.unical.dimes.repository.DBManager;
import it.unical.dimes.repository.FilmRepository;
import it.unical.dimes.repository.FilmRepositoryImpl;

import java.util.List;

public class TemporaryMain {


    public static void main(String[] args) {

        System.out.println("Inizio test");
        DBManager.getInstance().initDB();
        System.out.println("Inizializzazione..");

        FilmRepository repository = new FilmRepositoryImpl(DBManager.getInstance());

        List<Film> repo = repository.search(new FilmFilter.Builder().build());
        for(Film f : repo)
            System.out.println(f);



    }



}