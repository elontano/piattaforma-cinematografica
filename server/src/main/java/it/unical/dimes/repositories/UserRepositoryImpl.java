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
                    return new User(rs.getInt("id"),rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error during user search",e);
        }
        return null;
    }

    @Override
    public User save(User user) {
        String query = UserQueries.SAVE;

        try (Connection connection = dbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2,user.getPassword());

            int affectedRows = ps.executeUpdate();

            if(affectedRows==0)
                throw new SQLException("User creation failed, no rows modified");

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    user.setId(keys.getInt(1));
                }else {
                    throw new SQLException("User creation failed, ID not obtained");
                }
            }
        } catch (SQLException e) {
            //cod 1062 specifico per MySql
            if(e.getErrorCode()==1062){
                throw new UserAlreadyExistsException("User "+user.getUsername()+" already exists!");
            }
            //per tutti gli altri errori
            throw new RuntimeException("Save Error",e);
        }
        return user;
    }

}
