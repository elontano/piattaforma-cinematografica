package it.unical.dimes.controller;

import io.grpc.stub.StreamObserver;
import it.unical.dimes.entity.Film;
import it.unical.dimes.exception.CatalogException;
import it.unical.dimes.mapper.FilmMapper;
import it.unical.dimes.protocol.CatalogServiceGrpc;
import it.unical.dimes.protocol.FilmDTO;
import it.unical.dimes.protocol.OperationResponse;
import it.unical.dimes.service.FilmService;


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
            e.printStackTrace();
            response = OperationResponse.newBuilder()
                    .setValid(false)
                    .setMessage("Unknown Internal error.")
                    .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
