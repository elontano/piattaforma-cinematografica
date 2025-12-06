package it.unical.dimes.repository;

import it.unical.dimes.entity.Film;

import java.util.List;

public interface FilmRepository {

    Film save(Film film);

    List<Film> search(FilmFilter filter);

    //potrebbe ritornare il film aggiornato
    void update(Film film);

    //potrebbe ritornare il film eliminato
    void delete(Long ID);
}
