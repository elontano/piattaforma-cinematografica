package it.unical.dimes;


import it.unical.dimes.entity.Film;
import it.unical.dimes.entity.ViewingStatus;
import it.unical.dimes.repository.DBManager;
import it.unical.dimes.repository.FilmRepository;
import it.unical.dimes.repository.FilmRepositoryImpl;

public class Main {


    public static void main(String[] args) {

        System.out.println("Inizio test");
        DBManager.getInstance().initDB();
        System.out.println("Inizializzazione..");

        FilmRepository repository = new FilmRepositoryImpl(DBManager.getInstance());

        Film nuovoFilm2 = new Film.Builder("Santa Maradona")
                .yearOfRelease(2010)
                .genre("Commedia")
                .viewingStatus(ViewingStatus.WATCHED)
                .build();

        Film salvato = repository.save(nuovoFilm2);

        if(salvato!=null){
            System.out.println(salvato+" salvato con succ");
        }else{
            System.err.println("Errore salvataggoi");
        }

    }



}