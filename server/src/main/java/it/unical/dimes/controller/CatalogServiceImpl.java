package it.unical.dimes.controller;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.entities.SortBy;
import it.unical.dimes.exception.CatalogException;
import it.unical.dimes.mapper.FilmMapper;
import it.unical.dimes.mapper.ViewingStatusMapper;
import it.unical.dimes.protocol.*;
import it.unical.dimes.services.FilmService;

import java.util.List;

public class CatalogServiceImpl extends CatalogServiceGrpc.CatalogServiceImplBase {

    private final FilmService filmService;

    public CatalogServiceImpl(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    public void create(CreateFilmRequest request, StreamObserver<FilmDTO> responseObserver) {

        Film film = FilmMapper.fromCreateRequest(request);
        int userId = request.getUserId();

        FilmDTO response;
        try {
            Film savedFilm = filmService.save(film, userId);

            response = FilmMapper.toGrpc(savedFilm);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (CatalogException e) {
            responseObserver.onError(e.getGrpcStatus().withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("server error ")
                    .asRuntimeException());
        }
    }

    @Override
    public void searchFilms(SearchFilmRequest request, StreamObserver<FilmListResponse> responseObserver) {
        int userId = request.getUserId();
        try {
            FilmFilter filter = mapToFilter(request);
            List<Film> listFilm = filmService.search(filter, userId);
//            System.out.println("Trovati "+listFilm.size()+" film");
            FilmListResponse.Builder responseBuilder = FilmListResponse.newBuilder();

            for (Film film : listFilm) {
                FilmDTO filmDTO = FilmMapper.toGrpc(film);
                responseBuilder.addFilmList(filmDTO);
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error during search operation")
                    .asRuntimeException());
        }
    }

    @Override
    public void update(FilmDTO request, StreamObserver<OperationResponse> responseObserver) {

        Film film = FilmMapper.fromGrpc(request);
        int userId = request.getUserId();

        try {
            filmService.update(film, userId);

            OperationResponse response = OperationResponse.newBuilder()
                    .setValid(true)
                    .setMessage(film.getTitle() + " updated")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (CatalogException e) {
            responseObserver.onError(e.getGrpcStatus().withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("server error ").asRuntimeException());
        }
    }

    @Override
    public void delete(FilmIdRequest request, StreamObserver<OperationResponse> responseObserver) {
        int id = request.getId();
        int userId = request.getUserId();

        try {
            filmService.delete(id, userId);

            OperationResponse response = OperationResponse.newBuilder()
                    .setValid(true)
                    .setMessage("film with id: " + id + " deleted")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (CatalogException e) {
            responseObserver.onError(e.getGrpcStatus().withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("server error ").asRuntimeException());
        }
    }

    private FilmFilter mapToFilter(SearchFilmRequest request) {
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
