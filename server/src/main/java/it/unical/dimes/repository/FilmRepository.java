package it.unical.dimes.repository;

import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;

import java.util.List;

public interface FilmRepository {

    Film save(Film film);

    List<Film> search(FilmFilter filter);

    boolean update(Film ID);

    boolean delete(Integer ID);

    boolean exists(Integer ID);
}
