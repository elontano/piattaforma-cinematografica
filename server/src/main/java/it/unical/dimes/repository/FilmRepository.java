package it.unical.dimes.repository;

import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;

import java.util.List;

public interface FilmRepository {

    Film save(Film film,String userId);

    List<Film> search(FilmFilter filter,String userId);

    boolean update(Film ID,String userId);

    boolean delete(Integer ID,String userId);

}
