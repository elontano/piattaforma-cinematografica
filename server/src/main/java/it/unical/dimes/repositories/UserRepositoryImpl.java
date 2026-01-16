package it.unical.dimes.repositories;

import it.unical.dimes.entities.User;
import it.unical.dimes.exception.UserAlreadyExistsException;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository {
    private final DBManager dbManager;

    public UserRepositoryImpl(DBManager dbManager){
        this.dbManager = dbManager;
    }

    @Override
    public User findByUsername(String username) {
        String query = UserQueries.SEARCH;

        try (Connection connection = dbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    User user = new User(rs.getInt("id"),rs.getString("username"), rs.getString("password"));
                    return user;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore DB durante ricerca utente"+e.getMessage());
        }
        return null;
    }

    @Override
    public User save(User user) {
        System.out.println("Salvo utente "+user.getUsername());
        String query = UserQueries.SAVE;

        try (Connection connection = dbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2,user.getPassword());

            int affectedRows = ps.executeUpdate();

            if(affectedRows==0)
                throw new SQLException("Creazione utente fallita, nessuna riga modificata");

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }else {
                    throw new SQLException("Creazione utente fallita, ID non ottenuto");
                }

            }
        } catch (SQLException e) {
            if(e.getErrorCode()==1062){
                throw new UserAlreadyExistsException("L'utente "+user.getUsername()+" esiste già!");
            }
            //per tutti gli altri errori
            throw new RuntimeException("Errore salvataggio "+e.getMessage());
        }
        return user;
    }

}
