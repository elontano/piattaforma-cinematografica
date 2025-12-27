package it.unical.dimes.repositories;

import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;

import java.util.List;

public interface FilmRepository {

    Film save(Film film,int userId);

    List<Film> search(FilmFilter filter,int userId);

    boolean update(Film ID,int userId);

    boolean delete(Integer ID,int userId);

}
