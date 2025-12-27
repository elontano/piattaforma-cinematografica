package it.unical.dimes.repositories;

import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.entities.SortBy;
import it.unical.dimes.entities.ViewingStatus;

import java.util.List;

public class FilmQueries {
    private static final String TABLE = "Film";
    //nomi colonne
    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String TITLE = "title";
    private static final String DIRECTOR = "director";
    private static final String YEAR = "year_of_release";
    private static final String GENRE = "genre";
    private static final String RATING = "rating";
    private static final String STATUS = "viewing_status";

    public static final String INSERT = "INSERT INTO " + TABLE +
            String.format("(%s, %s, %s, %s, %s, %s, %s)",USER_ID, TITLE, DIRECTOR, YEAR, GENRE, RATING, STATUS) +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE = "UPDATE " + TABLE + " SET "
            + String.format("%s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=? AND %s=?",
            TITLE, DIRECTOR, YEAR, GENRE, RATING, STATUS, ID, USER_ID);

    public static final String DELETE = "DELETE FROM " + TABLE + " WHERE " + ID + " = ? AND " + USER_ID + " = ?";

    public static final String EXISTS = "SELECT COUNT(*) FROM " + TABLE + " WHERE " + ID + " = ? AND " + USER_ID + " = ?";

    public static String buildSearchQuery(FilmFilter filter, int useriD, List<Object> params){
        StringBuilder query = new StringBuilder("SELECT * FROM "+TABLE+" WHERE "+USER_ID+" = ?");

        params.add(useriD);

        if(filter.getTitle() !=null && !filter.getTitle().isEmpty()){
            query.append(" AND "+TITLE+" LIKE ? ");
            params.add("%"+filter.getTitle()+"%");
        }

        if(filter.getDirector() != null && !filter.getDirector().isEmpty()){
            query.append(" AND "+DIRECTOR+" LIKE ?");
            params.add("%"+filter.getDirector()+"%");
        }

        if(filter.getGenre() != null && !filter.getGenre().isEmpty()){
            query.append(" AND "+GENRE+" LIKE ?");
            params.add("%"+filter.getGenre()+"%");
        }

        if (filter.getRating() != null && filter.getRating() > 0) {
            query.append(" AND " + RATING + " = ?");
            params.add(filter.getRating());
        }

        if (filter.getYearOfRelease() !=null && filter.getYearOfRelease()!=0){
            query.append(" AND "+YEAR+" = ?");
            params.add(filter.getYearOfRelease());
        }

        if( filter.getViewingStatus() != null && filter.getViewingStatus() != ViewingStatus.UNKNOWN_STATUS){
            query.append(" AND "+STATUS+" = ?");
            params.add(filter.getViewingStatus().name());
        }

        if(!(filter.getSortBy() == SortBy.NONE)){
            query.append(" ORDER BY ");
            switch (filter.getSortBy()){
                case TITLE -> query.append(TITLE);
                case YEAR -> query.append(YEAR);
                case RATING -> query.append(RATING);
                default -> query.append(ID);
            }
            if(filter.getSortDirection()){
                query.append(" ASC");
            }else {
                query.append(" DESC");
            }
        }
        return query.toString();
    }
}
