package it.unical.dimes.repositories;

import it.unical.dimes.entities.User;

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
                    int id = (rs.getInt("id"));
                    String userN = (rs.getString("username"));
                    return new User(id,userN);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore ricerca utennte");
        }
        return null;
    }

    @Override
    public User save(User user) {
        String query = UserQueries.SAVE;

        try (Connection connection = dbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());

            int affectedRows = ps.executeUpdate();
            if(affectedRows==0)
                throw new SQLException("Creazione utente fallita");

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }else {
                    throw new SQLException("Creazione utente fallita");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore ricerca utenti");
        }
        return user;
    }

}
