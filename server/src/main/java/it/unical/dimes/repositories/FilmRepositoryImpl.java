package it.unical.dimes.repository;

import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.entities.ViewingStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmRepositoryImpl implements FilmRepository {
    //column names
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DIRECTOR = "director";
    private static final String YEAR = "year_of_release";
    private static final String GENRE = "genre";
    private static final String RATING = "rating";
    private static final String STATUS = "viewing_status";

    private final DBManager dbManager;

    public FilmRepositoryImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }


    public Film save(Film film,String userID) {
        //Film : ID,TITLE,DIRECTOR,YEAR,GENRE,RATIN,VS;
        String query = Queries.INSERT;
        int id = -1;
        try (Connection connection = dbManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1,userID);
            ps.setString(2, film.getTitle());
            setStringOrNull(ps, 3, film.getDirector());
            setIntOrNull(ps, 4, film.getYearOfRelease());
            setStringOrNull(ps, 5, film.getGenre());
            setIntOrNull(ps, 6, film.getRating());
            ps.setString(7, film.getViewingStatus().name());

            int affRows = ps.executeUpdate();

            if (affRows == 0) {
                throw new SQLException("Insert failed, no rows changed..");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Insert failed, no ID received..");
                }
            }
        } catch (SQLException e) {
            System.err.println("DB error during save..");
            throw new RuntimeException("DB error during save..", e);
        }


        return new Film.Builder(film.getTitle())
                .id(id)
                .userId(film.getUserId())
                .director(film.getDirector())
                .yearOfRelease(film.getYearOfRelease())
                .genre(film.getGenre())
                .rating(film.getRating())
                .viewingStatus(film.getViewingStatus())
                .build();
    }

    @Override
    public List<Film> search(FilmFilter filter, String userId) {

        //verrà popolato da buildSearchQuery
        List<Object> params = new ArrayList<>();
        String query = Queries.buildSearchQuery(filter,userId,params);

        List<Film> listFilm = new ArrayList<>();

        try(Connection connection = dbManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            for(int i=0;i<params.size();i++){
                ps.setObject(i+1,params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    listFilm.add(extractFilm(rs));
                }
            }
        }catch (SQLException e){
            System.err.println("DB connection failed"+e.getMessage());
            throw new RuntimeException("Errore durante la ricerca nel DB", e);
        }
        return listFilm;
    }

    @Override
    public boolean update(Film film,String userId) {
        //Film: ID,TITLE,DIRECTOR,YEAR,GENRE,RATING,VS;
        String query = Queries.UPDATE;
        int rowsAffected = 0;
        try (Connection connection = dbManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setString(1, film.getTitle());
            setStringOrNull(ps,2,film.getDirector());
            setIntOrNull(ps,3, film.getYearOfRelease());
            setStringOrNull(ps,4,film.getGenre());
            setIntOrNull(ps,5, film.getRating());
            ps.setString(6,film.getViewingStatus().name());

            //WHERE Film ID = ?
            ps.setInt(7,film.getId());
            //WHERE User ID = ?
            ps.setString(8,userId);

            rowsAffected = ps.executeUpdate();
        }catch (SQLException e ){
            throw new RuntimeException("DB error during update");
        }
        return rowsAffected>0;
    }

    @Override
    public boolean delete(Integer ID, String userId) {
        String query = Queries.DELETE;
        int rowsAffected = 0;
        try(Connection connection = dbManager.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1,ID);
            ps.setString(2,userId);

            rowsAffected = ps.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("DB error during delete");
        }
        return rowsAffected>0;
    }

    private Film extractFilm(ResultSet rs) throws SQLException {
        return new Film.Builder(rs.getString(TITLE))
                .id(rs.getInt(ID))
                .director(rs.getString(DIRECTOR))
                .genre(rs.getString(GENRE))
                .yearOfRelease(rs.getInt(YEAR))
                .rating(rs.getInt(RATING))
                .viewingStatus(ViewingStatus.valueOf(rs.getString(STATUS)))
                .build();
    }

    private void setStringOrNull(PreparedStatement ps, int index, String value) throws SQLException {

        if (value == null || value.isEmpty())
            ps.setNull(index, Types.VARCHAR);
        else ps.setString(index, value);
    }

    private void setIntOrNull(PreparedStatement ps, int index, Integer value) throws SQLException {

        if (value == null || value == 0)
            ps.setNull(index, Types.INTEGER);
        else ps.setInt(index, value);
    }
}
