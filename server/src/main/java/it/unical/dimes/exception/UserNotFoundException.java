package it.unical.dimes.exception;

import io.grpc.Status;

public class UserNotFoundException extends CatalogException{

    public UserNotFoundException(String message){
        super(message);
    }

    @Override
    public Status getGrpcStatus() {
        return Status.NOT_FOUND;
    }
}
