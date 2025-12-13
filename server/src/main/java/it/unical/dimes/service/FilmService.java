package it.unical.dimes.service;

import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.exception.FilmNotFoundException;
import it.unical.dimes.exception.ValidationException;
import it.unical.dimes.repository.FilmRepository;

import java.time.Year;
import java.util.List;

public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository){
        this.filmRepository=filmRepository;
    }

    public void save(Film film){
        validateFilm(film);
        filmRepository.save(film);
    }

    public List<Film> search(FilmFilter filter){
        return filmRepository.search(filter);
    }

    public void update(Film film){
        validateFilm(film);
        if(!filmRepository.update(film))
            throw new FilmNotFoundException(film.getTitle()+" not found to update");
    }

    public void delete(Integer id){
        if(!filmRepository.delete(id))
            throw new FilmNotFoundException("Film with id "+id+"not found");
    }

    private void validateFilm(Film film) {
        if (film == null) throw new ValidationException("Film data cannot be null");

        if (film.getTitle() == null || film.getTitle().isEmpty())
            throw new ValidationException("The title of the film must be specified.");

        if (film.getRating() != null && film.getRating() != 0) {
            if (film.getRating() < 1 || film.getRating() > 5)
                throw new ValidationException("The rating must be between 1 and 5.");
        }

        //    Range Film valido dal 1895 (primo film della storia) al prossimo anno (film che uscirà in futuro)
        int annoCorrente = Year.now().getValue();
        if (film.getYearOfRelease() != null && film.getYearOfRelease() != 0) {
            if (film.getYearOfRelease() < 1895 || film.getYearOfRelease() > annoCorrente + 1) {
                throw new ValidationException("Invalid Year: valid range from 1895 to " + (annoCorrente + 1));
            }
        }
    }
}
