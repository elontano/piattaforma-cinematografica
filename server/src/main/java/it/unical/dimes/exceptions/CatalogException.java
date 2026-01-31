package it.unical.dimes.exceptions;


import io.grpc.Status;

public abstract class CatalogException extends RuntimeException{

    public CatalogException(String message){
        super(message);
    }

    public abstract Status getGrpcStatus();
}
