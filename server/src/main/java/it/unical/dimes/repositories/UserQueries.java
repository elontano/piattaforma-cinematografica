package it.unical.dimes.repositories;

public class UserQueries {

    private static final String TABLE = "Users";
    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static final String SEARCH = "SELECT "+ID+", "+USERNAME+", "+PASSWORD+" FROM "+TABLE+" WHERE "+USERNAME+" = ?";

    public static final String SAVE = "INSERT INTO "+TABLE+" ("+USERNAME+", "+PASSWORD+") VALUES (?, ?)";

}
