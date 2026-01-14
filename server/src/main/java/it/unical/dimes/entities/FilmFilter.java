package it.unical.dimes.entities;

public class FilmFilter {

    private final String title;
    private final String director ;
    private final String genre;
    private final Integer yearOfRelease;
    private final Integer rating;
    private final ViewingStatus viewingStatus;
    private final SortBy sortBy;
    private final boolean sortDirection;

    public static class Builder{

        private String title = "";
        private String director = "";
        private String genre = "";
        private Integer yearOfRelease = 0;
        private Integer rating = 0;
        private ViewingStatus viewingStatus = ViewingStatus.UNKNOWN_STATUS; //default
        private SortBy sortBy = SortBy.NONE;
        private boolean sortDirection = true; //true ascen //false

        public Builder title(String title){
            this.title=title;
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

        public Builder sortBy(SortBy sortBy){
            this.sortBy=sortBy;
            return this;
        }

        public Builder sortDirection(boolean sortDirection){
            this.sortDirection=sortDirection;
            return this;
        }

        public FilmFilter build(){
            return new FilmFilter(this);
        }
    }

    private FilmFilter(Builder builder){
        title=builder.title;
        director=builder.director;
        genre=builder.genre;
        yearOfRelease=builder.yearOfRelease;
        rating=builder.rating;
        viewingStatus=builder.viewingStatus;
        sortBy=builder.sortBy;
        sortDirection=builder.sortDirection;
    }

    public String getDirector() {
        return director;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getRating() {
        return rating;
    }

    public SortBy getSortBy() {
        return sortBy;
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

    public boolean getSortDirection(){
        return sortDirection;
    }

    @Override
    public String toString() {
        return "FilmFilter{" +
                "director='" + director + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", yearOfRelease=" + yearOfRelease +
                ", rating=" + rating +
                ", viewingStatus=" + viewingStatus +
                ", sortBy=" + sortBy +
                ", sortDirection=" + sortDirection +
                '}';
    }
}
