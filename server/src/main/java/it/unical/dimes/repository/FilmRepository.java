package it.unical.dimes.repository;

import it.unical.dimes.entity.Film;
import it.unical.dimes.entity.FilmFilter;

import java.util.List;

public interface FilmRepository {

    /*
    ritorna il film salvato con l'ID
     */
    Film save(Film film);

    List<Film> search(FilmFilter filter);

    //potrebbe ritornare il film aggiornato
    boolean update(Film ID);

    //potrebbe ritornare il film eliminato
    boolean delete(Integer ID);
}
