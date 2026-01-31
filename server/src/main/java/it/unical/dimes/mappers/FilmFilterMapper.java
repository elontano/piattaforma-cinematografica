package it.unical.dimes.mappers;

import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.entities.SortBy;
import it.unical.dimes.protocol.SearchFilmRequest;
import it.unical.dimes.protocol.SortByDTO;
import it.unical.dimes.protocol.ViewingStatusDTO;

public class FilmFilterMapper {


    public static FilmFilter mapToFilter(SearchFilmRequest request) {
        FilmFilter.Builder builder = new FilmFilter.Builder();

        if (request.hasTitle()) {
            builder.title(request.getTitle());
        }
        if (request.hasDirector()) {
            builder.director(request.getDirector());
        }
        if (request.hasGenre()) {
            builder.genre(request.getGenre());
        }
        if (request.hasYearOfRelease()) {
            builder.yearOfRelease(request.getYearOfRelease());
        }
        if (request.getViewingStatus() != ViewingStatusDTO.UNKNOWN_STATUS) {
            builder.viewingStatus(ViewingStatusMapper.fromGrpc(request.getViewingStatus()));
        }
        if (request.getSortBy() != SortByDTO.NONE) {
            builder.sortBy(SortBy.valueOf(request.getSortBy().name()));
            builder.sortDirection(request.getSortAscending());
        }
        return builder.build();
    }
}
