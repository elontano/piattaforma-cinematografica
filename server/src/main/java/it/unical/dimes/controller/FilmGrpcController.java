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
import java.util.logging.Logger;

public class FilmGrpcController extends FilmServiceGrpc.FilmServiceImplBase {

    private static final Logger logger = Logger.getLogger(FilmGrpcController.class.getName());

    private final FilmService filmService;

    public FilmGrpcController(FilmService filmService) {
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
            logger.severe("Error during create operation: " + e.getMessage());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("server error ")
                    .asRuntimeException());
            logger.severe("Error during create operation: " + e.getMessage());
        }
    }

    @Override
    public void searchFilms(SearchFilmRequest request, StreamObserver<FilmListResponse> responseObserver) {
        int userId = request.getUserId();
        try {
            FilmFilter filter = mapToFilter(request);
            List<Film> listFilm = filmService.search(filter, userId);

            FilmListResponse.Builder responseBuilder = FilmListResponse.newBuilder();

            responseBuilder.addAllFilmList(
                    listFilm.stream()
                            .map(FilmMapper::toGrpc)
                            .toList()
            );

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error during search operation")
                    .asRuntimeException());
            logger.severe("Error during search operation: " + e.getMessage());
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
            logger.severe("Error during update operation: " + e.getMessage());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("server error ").asRuntimeException());
            logger.severe("Error during update operation: " + e.getMessage());

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
            logger.severe("Error during delete operation: " + e.getMessage());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("server error ").asRuntimeException());
            logger.severe("Error during delete operation: " + e.getMessage());
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
