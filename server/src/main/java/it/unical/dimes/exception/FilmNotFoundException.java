package it.unical.dimes.exception;

public class FilmNotFoundException extends RuntimeException{

    public FilmNotFoundException(String message){
        super(message);
    }

}
