package it.unical.dimes.exception;

public abstract class CatalogException extends RuntimeException{

    public CatalogException(String message){
        super(message);
    }

}
