package it.unical.dimes.exception;

import io.grpc.Status;

public class ValidationException extends CatalogException {

    public ValidationException(String message){
        super(message);
    }

    @Override
    public Status getGrpcStatus() {
        return Status.INVALID_ARGUMENT;
    }


}
