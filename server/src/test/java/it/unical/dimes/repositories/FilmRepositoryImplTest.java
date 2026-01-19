package it.unical.dimes.repositories;

import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.entities.ViewingStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Film Repository Test with H2")
class FilmRepositoryImplTest {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @Mock
    private DBManager dbManager;

    private FilmRepositoryImpl filmRepository;

    private int userId;
    private Film film;

    @BeforeEach
    void setUp() throws SQLException {
        userId = 1;
        film = new Film.Builder("Avatar")
                .userId(userId)
                .build();

        Connection h2Connection = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
        when(dbManager.getConnection()).thenAnswer(invocationOnMock -> DriverManager.getConnection(JDBC_URL,USER,PASSWORD));

        filmRepository = new FilmRepositoryImpl(dbManager);
        initDB(h2Connection);
    }

    @AfterEach
    void tearDown() throws SQLException{
        try (Connection conn = DriverManager.getConnection(JDBC_URL,USER,PASSWORD)){
            Statement statement = conn.createStatement();{
                statement.execute("DROP ALL OBJECTS");
            }
        }
    }

    @Test
    @DisplayName("Should save a film on DB")
    void shouldSaveFilm(){

        Film saved = filmRepository.save(film,userId);

        assertNotNull(saved);
        assertTrue(saved.getId()>0);
        assertEquals("Avatar",saved.getTitle());
        assertEquals(userId,saved.getUserId());
    }

    @Test
    @DisplayName("Should find film ")
    void shouldFindFilm(){
        Film filmToSearch = new Film.Builder("Dune")
                .director("Villeneuve")
                .rating(5)
                .yearOfRelease(2021)
                .genre("avventura")
                .viewingStatus(ViewingStatus.WATCHED)
                .build();

        insertFilm(filmToSearch);

        FilmFilter filmFilter = new FilmFilter.Builder().title("Dune").build();

        List<Film> result = filmRepository.search(filmFilter,userId);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(result.get(0).getTitle(),filmToSearch.getTitle());
    }

    @Test
    @DisplayName("Should update an existing film")
    void shouldUpdateFilm(){

        Film old = new Film.Builder("oldtitle").director("olddirector").userId(userId).build();

        int filmId = insertFilm(old);

        Film filmToUpdate = new Film.Builder("newtitle").id(filmId).director("newdirector").userId(userId).build();

        boolean updated = filmRepository.update(filmToUpdate,userId);

        assertTrue(updated);

        FilmFilter filmFilter = new FilmFilter.Builder().title("newtitle").build();
        List<Film> result = filmRepository.search(filmFilter,userId);
        assertEquals(1,result.size());
    }

    @Test
    @DisplayName("should delete")
    void shouldDeleteFilm() {

        Film filmToDelete = new Film.Builder("toDelete").userId(userId).build();

        int id = insertFilm(filmToDelete);
        boolean deleted = filmRepository.delete(id, userId);

        assertTrue(deleted);

        FilmFilter filmFilter = new FilmFilter.Builder().title("toDelete").build();
        List<Film> result = filmRepository.search(filmFilter,userId);
        assertTrue(result.isEmpty());

    }

    void initDB(Connection h2Connection) throws SQLException{
        try (Statement statement = h2Connection.createStatement()){
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS Users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
                    );
                """);

            statement.execute("INSERT INTO Users (id,username,password) VALUES (1, 'testuser','password')");

            statement.execute("""
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
                """);
        }
    }

    private int insertFilm(Film film) {
        String query = FilmQueries.INSERT;
        try (Connection connection = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            //foreign key
            ps.setInt(1, userId);
            //dati film
            ps.setString(2, film.getTitle());
            ps.setString(3, film.getDirector());
            ps.setInt(4, film.getYearOfRelease());
            ps.setString(5, film.getGenre());
            ps.setInt(6, film.getRating());
            ps.setString(7, film.getViewingStatus().name());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return 0;
    }
}