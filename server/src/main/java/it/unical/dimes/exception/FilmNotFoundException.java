package it.unical.dimes.exception;


import io.grpc.Status;

public class FilmNotFoundException extends CatalogException{

    public FilmNotFoundException(String message){
        super(message);
    }

    @Override
    public Status getGrpcStatus() {
        return Status.NOT_FOUND;
    }

}
