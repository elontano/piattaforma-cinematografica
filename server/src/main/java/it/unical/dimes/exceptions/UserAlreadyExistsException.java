package it.unical.dimes.exceptions;

import io.grpc.Status;

public class UserAlreadyExistsException extends CatalogException{
    public UserAlreadyExistsException(String message){
        super(message);
    }

    @Override
    public Status getGrpcStatus() {
        return Status.ALREADY_EXISTS;
    }
}
