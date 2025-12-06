package it.unical.dimes.mapper;

import it.unical.dimes.entity.Film;
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
        return new FilmDTO.Builder()
                .setId(film.getId())
                .setTitle(film.getTitle())
                .setDirector(film.getDirector())
                .setGenre(film.getGenre())
                .setRating(film.getRating())
                .setYearOfRelease(film.getYearOfRelease())
                .setViewingStatus(ViewingStatusMapper.toGrpc(film.getViewingStatus()))
                .build();
    }


}
