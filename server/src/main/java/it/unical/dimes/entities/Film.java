package it.unical.dimes.entities;

import java.util.Objects;

public class Film {
    private final Integer id;
    private final Integer userId;
    private final String title;
    private final String director ;
    private final String genre;
    private final Integer yearOfRelease;
    private final Integer rating;
    private final ViewingStatus viewingStatus;

    public static class Builder{
        //required parameters
        private final String title;

        //optional
        private Integer id = null;
        private Integer userId = null;
        private String director = "";
        private String genre = "";
        private Integer yearOfRelease = 0;
        private Integer rating = 0;
        private ViewingStatus viewingStatus = ViewingStatus.TO_WATCH; //default

        public Builder(String title){
            this.title=title;
        }

        public Builder id(Integer id){
            this.id=id;
            return this;
        }

        public Builder userId(Integer userId){
            this.userId = userId;
            return this;
        }

        public Builder director(String director){
            this.director=director;
            return this;
        }

        public Builder genre(String genre){
            this.genre=genre;
            return this;
        }

        public Builder yearOfRelease(Integer yearOfRelease){
            this.yearOfRelease=yearOfRelease;
            return this;
        }

        public Builder rating(Integer rating){
            this.rating=rating;
            return this;
        }

        public Builder viewingStatus(ViewingStatus viewingStatus){
            this.viewingStatus=viewingStatus;
            return this;
        }

        public Film build(){
            return new Film(this);
        }
    }

    private Film (Builder builder){
        id=builder.id;
        userId = builder.userId;;
        title=builder.title;
        director=builder.director;
        genre=builder.genre;
        yearOfRelease=builder.yearOfRelease;
        rating=builder.rating;
        viewingStatus=builder.viewingStatus;
    }

    public Integer getUserId(){return userId;}

    public String getDirector() {
        return director;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public ViewingStatus getViewingStatus() {
        return viewingStatus;
    }

    public Integer getYearOfRelease() {
        return yearOfRelease;
    }

    @Override
    public String toString() {
        return "Film{" +
                "director='" + director + '\'' +
                ", id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", rating=" + rating +
                ", viewingStatus=" + viewingStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if( o == this) return true;
        if( ! (o instanceof Film)) return false;
        Film film = (Film) o;
        if(this.id != null && film.id != null)
            return Objects.equals(id,film.id);
        return Objects.equals(userId,film.userId) &&
                Objects.equals(title,film.title) &&
                Objects.equals(director,film.director) &&
                Objects.equals(yearOfRelease, film.yearOfRelease);
    }

    @Override
    public int hashCode() {
        if(id != null) return Objects.hashCode(id);
        return Objects.hash(userId,title,director,yearOfRelease);
    }
}
