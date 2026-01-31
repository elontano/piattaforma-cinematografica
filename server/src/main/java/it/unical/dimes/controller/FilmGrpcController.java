package it.unical.dimes.controller;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.exceptions.CatalogException;
import it.unical.dimes.mappers.FilmFilterMapper;
import it.unical.dimes.mappers.FilmMapper;
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
            logger.severe("Catalog error during create operation: " + e.getMessage());
            responseObserver.onError(e.getGrpcStatus().withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            logger.severe("Unexpected error during film creation: " + e.getMessage());

            responseObserver.onError(Status.INTERNAL.withDescription("Internal Server Error")
                    .asRuntimeException());
        }
    }

    @Override
    public void searchFilms(SearchFilmRequest request, StreamObserver<FilmListResponse> responseObserver) {
        int userId = request.getUserId();
        try {
            FilmFilter filter = FilmFilterMapper.mapToFilter(request);
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
            logger.severe("Error during search operation for user: " + userId);

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
                    .setMessage(film.getTitle() + " successfully updated")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (CatalogException e) {
            logger.severe("Catalog error during update operation: " + e.getMessage());
            responseObserver.onError(e.getGrpcStatus().withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            logger.severe("Unexpected error during update operation: " + e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription("Internal Server Error ").asRuntimeException());
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
                    .setMessage("Film with ID: " + id + " successfully deleted")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (CatalogException e) {
            logger.severe("Catalog error during delete operation: " + e.getMessage());
            responseObserver.onError(e.getGrpcStatus().withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            logger.severe("Unexpected error during delete operation: " + e.getMessage());
            responseObserver.onError(Status.INTERNAL.withDescription("Internal Server Error ").asRuntimeException());
        }
    }
}
