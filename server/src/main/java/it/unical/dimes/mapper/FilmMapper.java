package it.unical.dimes.mapper;

import it.unical.dimes.entities.Film;
import it.unical.dimes.protocol.CreateFilmRequest;
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
        FilmDTO.Builder filmDTOBuilder = FilmDTO.newBuilder();

        filmDTOBuilder.setTitle(film.getTitle() != null ? film.getTitle() : "");

        filmDTOBuilder.setDirector(film.getDirector() != null ? film.getDirector() : "");

        filmDTOBuilder.setGenre(film.getGenre() != null ? film.getGenre() : "");

        filmDTOBuilder.setRating(film.getRating())
                .setYearOfRelease(film.getYearOfRelease());

        if (film.getViewingStatus() != null) {
            filmDTOBuilder.setViewingStatus(ViewingStatusMapper.toGrpc(film.getViewingStatus()));
        } else {
        }

        // 6. Gestione ID opzionale
        if(film.getId() != null){
            filmDTOBuilder.setId(film.getId());
        }

        return filmDTOBuilder.build();
    }

    public static Film fromCreateRequest(CreateFilmRequest createFilmRequest){
        return new Film.Builder(createFilmRequest.getTitle())
                .director(createFilmRequest.getDirector())
                .genre(createFilmRequest.getGenre())
                .rating(createFilmRequest.getRating())
                .yearOfRelease(createFilmRequest.getYearOfRelease())
                .viewingStatus(ViewingStatusMapper.fromGrpc(createFilmRequest.getViewingStatus()))
                .build();
    }
}
