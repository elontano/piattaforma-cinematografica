package it.unical.dimes.repositories;

import it.unical.dimes.entities.User;
import it.unical.dimes.exception.CatalogException;
import it.unical.dimes.exception.UserAlreadyExistsException;
import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Repository Test with H2")
class UserRepositoryImplTest {

    private static final String JDBC_URL = "jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    @Mock
    private DBManager dbManager;

    private UserRepositoryImpl userRepository;

    private String username = "test";
    private String password = "password";

    @BeforeEach
    void setUp() throws SQLException{

        Connection h2Connection = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
        when(dbManager.getConnection()).thenAnswer(invocationOnMock -> DriverManager.getConnection(JDBC_URL,USER,PASSWORD));

        userRepository = new UserRepositoryImpl(dbManager);

        initDb(h2Connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL,USER,PASSWORD)){
            Statement statement = conn.createStatement();{
                statement.execute("DROP ALL OBJECTS");
            }
        }
    }

    @Nested
    @DisplayName("Find tests")
    class FindTests {
        @Test
        @DisplayName("Should find an User by Username")
        void shouldFindUserByUsername() {
            insertUser(username, password);

            User foundUser = userRepository.findByUsername(username);


            assertNotNull(foundUser);
            assertEquals(foundUser.getUsername(), username);
            assertEquals(foundUser.getPassword(), password);
            assertTrue(foundUser.getId() > 0);

        }

        @Test
        @DisplayName("Should return null if user not found")
        void shouldReturnNull() {
            User foundUser = userRepository.findByUsername(username);
            assertNull(foundUser);
        }
    }


    @Nested
    @DisplayName("Save tests")
    class SaveTests{
        @Test
        @DisplayName("Should save a new user")
        void shouldSaveUser(){
            User toSave = new User("new","newpass");

            User saved = userRepository.save(toSave);

            assertNotNull(saved);
            assertTrue(saved.getId()>0);
            assertEquals(toSave.getUsername(),saved.getUsername());
        }

        @Test
        @DisplayName("Should not save an existing user")
        void shouldNotSaveUseraAlreadyExixts(){
            User toSave = new User(username,password);
            userRepository.save(toSave);

            User duplicateUser = new User(username,"password2");

            //UserAlreadyExistsException ma MySQL e H2 hanno codici diversi
            assertThrows(RuntimeException.class,()->userRepository.save(duplicateUser));
        }
    }




    private void initDb(Connection h2Connection) throws SQLException {
        try (Statement statement = h2Connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS Users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL
                    );
                """);
        }
    }

    private void insertUser(String username,String password) {
        String query = UserQueries.SAVE;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}