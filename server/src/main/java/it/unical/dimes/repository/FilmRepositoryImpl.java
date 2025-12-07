package it.unical.dimes.repository;

import it.unical.dimes.entity.Film;
import it.unical.dimes.entity.FilmFilter;

import java.sql.*;
import java.util.List;

public class FilmRepositoryImpl implements FilmRepository{

    private final DBManager dbManager;

    public FilmRepositoryImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Film save(Film film) {
        String query = "INSERT INTO Film(title,director,year_of_release,genre,rating,viewing_status) " +
                "VALUES (?, ?, ?, ?, ?, ?) ";

        int id = -1;
        try (Connection connection = dbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, film.getTitle());

            if (film.getDirector()==null || film.getDirector().isEmpty()) ps.setNull(2, Types.VARCHAR);
            else ps.setString(2, film.getDirector());

            if (film.getYearOfRelease()==null || film.getYearOfRelease() <= 1895) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, film.getYearOfRelease());

            if (film.getGenre()==null || film.getGenre().isEmpty()) ps.setNull(4, Types.VARCHAR);
            else ps.setString(4, film.getGenre());

            if (film.getRating()==null || film.getRating() == 0) ps.setNull(5, Types.INTEGER);
            else ps.setInt(5, film.getRating());

            ps.setString(6, film.getViewingStatus().name());

            int affRows = ps.executeUpdate();

            if (affRows == 0) {
                throw new SQLException("Inserimento fallito, nessuna riga modificata.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserimento fallito, nessun ID ottenuto.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //throw new RuntimeException("Errore DB durante il salvataggio", e);
        }

        if(id!=-1){
            return new Film.Builder(film.getTitle())
                    .id(id)
                    .director(film.getDirector())
                    .yearOfRelease(film.getYearOfRelease())
                    .genre(film.getGenre())
                    .rating(film.getRating())
                    .viewingStatus(film.getViewingStatus())
                    .build();
        }
        return null;
    }

    @Override
    public List<Film> search(FilmFilter filter) {
        return List.of();
    }

    @Override
    public void update(Film film) {

    }

    @Override
    public void delete(Long ID) {

    }
}
