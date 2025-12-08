package it.unical.dimes.repository;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static DBManager instance = null;
    private final MysqlDataSource ds;

    private DBManager(){
        ds = new MysqlDataSource();
        try {
            ds.setServerName("127.0.0.1");
            ds.setPort(3306);
            ds.setUser("root");
            ds.setPassword("elontano");
            ds.setDatabaseName("piattaforma_cinematografica");

            ds.setUseSSL(false);
            ds.setAllowPublicKeyRetrieval(true);
        }catch (SQLException e){
            System.err.println("Errore configurazione Data Source "+e.getMessage());
            e.printStackTrace();
        }
    }

    public static DBManager getInstance(){
        if(instance == null)
            instance = new DBManager();
        return instance;
    }

    public Connection getConnection() throws SQLException{
        return ds.getConnection();
    }

    public void initDB(){
        String creaTabella = """
            CREATE TABLE IF NOT EXISTS Film (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                director VARCHAR(255),
                year_of_release INT,
                genre VARCHAR(100),
                rating INT,
                viewing_status VARCHAR(50)
            );
        """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(creaTabella);
            System.out.println("Tabella 'Film' creata/verificata con successo.");
        } catch (SQLException e) {
            System.err.println("initDB error " + e.getMessage());
        }
    }
}
