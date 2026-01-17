package it.unical.dimes.repositories;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBManager {
    private static DBManager instance = null;
    private final MysqlDataSource ds;

    //TODO Connection Pool da implementare in futuro per aumentare scalabilità

    private DBManager() {
        ds = new MysqlDataSource();
        Properties props = new Properties();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("File 'config.properties' not found!");
            }
            props.load(inputStream);

            ds.setServerName(props.getProperty("server.name"));
            ds.setPort(Integer.parseInt(props.getProperty("port.number")));
            ds.setUser(props.getProperty("db.user"));
            ds.setPassword(props.getProperty("db.password"));
            ds.setDatabaseName(props.getProperty("db.name"));

            ds.setUseSSL(false);
            ds.setAllowPublicKeyRetrieval(true);
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        } catch (SQLException e) {
            throw new RuntimeException("MySQL parameter configuration error", e);
        }
    }

    public static synchronized DBManager getInstance() {
        if (instance == null)
            instance = new DBManager();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public void initDB() {

        String createTableUsers = """
                    CREATE TABLE IF NOT EXISTS Users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
                    );
                """;

        String createTableFilm = """
                    CREATE TABLE IF NOT EXISTS Film (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        title VARCHAR(255) NOT NULL,
                        director VARCHAR(255),
                        year_of_release INT,
                        genre VARCHAR(100),
                        rating INT,
                        viewing_status VARCHAR(50),
                        FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
                    );
                """;
        //ON DELETE CASCADE: se elimino l'utente elimino i suoi film

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableUsers);
            System.out.println("'Users' table verified.");
            statement.execute(createTableFilm);
            System.out.println("'Film' table successfully created/verified.");
        } catch (SQLException e) {
            System.err.println("initDB error " + e.getMessage());
        }
    }
}
