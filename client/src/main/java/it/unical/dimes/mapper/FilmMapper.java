package it.unical.dimes.mapper;

import it.unical.dimes.model.Film;
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
        }

        if(film.getId() != null){
            filmDTOBuilder.setId(film.getId());
        }

        return filmDTOBuilder.build();
    }

    public static CreateFilmRequest fromCreateFilmRequest(FilmDTO filmDTO){
        return CreateFilmRequest.newBuilder()
                .setDirector(filmDTO.getDirector())
                .setGenre(filmDTO.getGenre())
                .setRating(filmDTO.getRating())
                .setTitle(filmDTO.getTitle())
                .setUserId(filmDTO.getUserId())
                .setViewingStatus(filmDTO.getViewingStatus())
                .setYearOfRelease(filmDTO.getYearOfRelease())
                .build();
    }
}