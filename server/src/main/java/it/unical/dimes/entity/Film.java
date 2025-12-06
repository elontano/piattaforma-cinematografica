package it.unical.dimes.entity;

public class Film {
    private final Long id;
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
        private Long id = null;
        private String director = "";
        private String genre = "";
        private Integer yearOfRelease = 0;
        private Integer rating = 0;
        private ViewingStatus viewingStatus = ViewingStatus.TO_WATCH; //default

        public Builder(String title){
            this.title=title;
        }

        public Builder id(Long id){
            this.id=id;
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
        title=builder.title;
        director=builder.director;
        genre=builder.genre;
        yearOfRelease=builder.yearOfRelease;
        rating=builder.rating;
        viewingStatus=builder.viewingStatus;
    }

    @Override
    public String toString() {
        return "Film {id= "+id+" title= '"+title+"'";
        //da completare
    }
}
