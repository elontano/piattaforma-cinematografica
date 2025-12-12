package it.unical.dimes.controller;

import io.grpc.stub.StreamObserver;
import it.unical.dimes.entity.Film;
import it.unical.dimes.entity.FilmFilter;
import it.unical.dimes.entity.SortBy;
import it.unical.dimes.entity.ViewingStatus;
import it.unical.dimes.exception.CatalogException;
import it.unical.dimes.exception.FilmNotFoundException;
import it.unical.dimes.mapper.FilmMapper;
import it.unical.dimes.mapper.ViewingStatusMapper;
import it.unical.dimes.protocol.*;
import it.unical.dimes.service.FilmService;

import java.util.List;

public class CatalogServiceImpl extends CatalogServiceGrpc.CatalogServiceImplBase {

    private final FilmService filmService;

    public CatalogServiceImpl(FilmService filmService){
        this.filmService=filmService;
    }

    @Override
    public void create(FilmDTO request, StreamObserver<OperationResponse> responseObserver) {
        Film film = FilmMapper.fromGrpc(request);
        OperationResponse response;
        try {
            filmService.save(film);
             response = OperationResponse.newBuilder()
                    .setValid(true)
                    .setMessage("Film saved succesfully")
                    .build();

        }catch (CatalogException e){
            System.err.println("Errore "+e.getMessage());
            response = OperationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Error "+e.getMessage())
                    .build();

        }catch (Exception e){
            response = OperationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Unknown Internal error.")
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void searchFilms(SearchFilmRequest request, StreamObserver<FilmListResponse> responseObserver) {
        FilmFilter filter = mapToFilter(request);
        List<Film> listFilm = filmService.search(filter);
        System.out.println("Trovati "+listFilm.size()+" film");
        FilmListResponse.Builder responseBuilder = FilmListResponse.newBuilder();

        for(Film film : listFilm){
            FilmDTO filmDTO = FilmMapper.toGrpc(film);
            responseBuilder.addFilmList(filmDTO);
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void update(FilmDTO request, StreamObserver<OperationResponse> responseObserver) {
        Film film = FilmMapper.fromGrpc(request);
        OperationResponse response;
        try {
            filmService.update(film);
            response = OperationResponse.newBuilder()
                    .setValid(true)
                    .setMessage(film.getTitle()+" updated")
                    .build();
        }catch (FilmNotFoundException ffe){
            response = OperationResponse.newBuilder()
                    .setValid(false)
                    .setMessage(film.getTitle()+" not found!")
                    .build();
        }catch (Exception e){
            response = OperationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Unknown Internal Error ")
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void delete(FilmIdRequest request, StreamObserver<OperationResponse> responseObserver) {
        Integer id = request.getId();
        OperationResponse response;
        try {
            filmService.delete(id);
            response = OperationResponse.newBuilder()
                    .setValid(true)
                    .setMessage("film with id: "+id+" deleted")
                    .build();
        }catch (FilmNotFoundException ffe){
            response = OperationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("film with id: "+id+" not found!")
                    .build();
        }catch (Exception e){
            response = OperationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Unknown Internal Error ")
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private FilmFilter mapToFilter(SearchFilmRequest request){
        FilmFilter.Builder builder = new FilmFilter.Builder();
        if(request.hasTitle()) {
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
            // Esempio: mappa l'enum gRPC in una enum di dominio o stringa
            builder.sortBy(SortBy.valueOf(request.getSortBy().name()));
            builder.sortDirection(request.getSortAscending());
        }
        return builder.build();
    }
}
