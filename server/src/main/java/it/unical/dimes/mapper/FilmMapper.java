package it.unical.dimes.mapper;

import it.unical.dimes.entities.Film;
import it.unical.dimes.protocol.FilmDTO;

public class FilmMapper {

    public static Film fromGrpc(FilmDTO filmDTO){
        return new Film.Builder(filmDTO.getTitle())
                .id(filmDTO.getId())
                .director(filmDTO.getDirector())
                .genre(filmDTO.getGenre())
                .rating(filmDTO.getRating())
                .yearOfRelease(filmDTO.getYearOfRelease())
                .viewingStatus(ViewingStatusMapper.fromGrpc(filmDTO.getViewingStatus()))
                .build();
    }

    public static FilmDTO toGrpc (Film film){
        // Inizio creazione Builder
        FilmDTO.Builder filmDTOBuilder = FilmDTO.newBuilder();

        // 1. Gestione sicura delle Stringhe (Title)
        // Se il titolo è null, mettiamo stringa vuota o un placeholder per evitare crash
        filmDTOBuilder.setTitle(film.getTitle() != null ? film.getTitle() : "");

        // 2. LA FIX PER IL TUO ERRORE (Director)
        // Se getDirector è null, passiamo "" (stringa vuota)
        filmDTOBuilder.setDirector(film.getDirector() != null ? film.getDirector() : "");

        // 3. Gestione sicura per Genre (consigliato fare lo stesso)
        filmDTOBuilder.setGenre(film.getGenre() != null ? film.getGenre() : "");

        // 4. Campi primitivi (int/double)
        // Questi non danno solitamente NPE a meno che nel model "Film" non siano Integer/Double oggetti
        filmDTOBuilder.setRating(film.getRating())
                .setYearOfRelease(film.getYearOfRelease());

        // 5. Gestione Oggetti complessi (ViewingStatus)
        if (film.getViewingStatus() != null) {
            filmDTOBuilder.setViewingStatus(ViewingStatusMapper.toGrpc(film.getViewingStatus()));
        } else {
            // Opzionale: gestire il caso in cui lo status sia null,
            // magari impostando un valore di default definito nel tuo proto (es. UNKNOWN)
        }

        // 6. Gestione ID opzionale
        if(film.getId() != null){
            filmDTOBuilder.setId(film.getId());
        }

        return filmDTOBuilder.build();
    }
}
