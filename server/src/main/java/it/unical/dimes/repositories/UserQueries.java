package it.unical.dimes.repositories;

public class UserQueries {

    private static final String TABLE = "Users";
    private static final String ID = "id";
    private static final String USERNAME = "username";

    public static final String SEARCH = "SELECT "+ID+", "+USERNAME+" FROM "+TABLE+" WHERE "+USERNAME+" = ?";

    public static final String SAVE = "INSERT INTO "+TABLE+" ("+USERNAME+") VALUES (?)";

}
