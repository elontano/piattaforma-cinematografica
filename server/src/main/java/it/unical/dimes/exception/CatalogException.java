package it.unical.dimes.exception;


import io.grpc.Status;

public abstract class CatalogException extends RuntimeException{

    public CatalogException(String message){
        super(message);
    }

    public abstract Status getGrpcStatus();
}
