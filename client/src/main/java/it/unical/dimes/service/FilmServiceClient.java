package it.unical.dimes.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.entities.SortBy;
import it.unical.dimes.entities.ViewingStatus;
import it.unical.dimes.mapper.FilmMapper;
import it.unical.dimes.mapper.SortByMapper;
import it.unical.dimes.mapper.ViewingStatusMapper;
import it.unical.dimes.protocol.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FilmServiceClient {

    private final ManagedChannel channel;
    private final CatalogServiceGrpc.CatalogServiceBlockingStub blockingStub;

    public FilmServiceClient(String host, int port){
        this.channel = ManagedChannelBuilder.forAddress(host,port)
                .usePlaintext()//Senza SSL
                .build();
        this.blockingStub = CatalogServiceGrpc.newBlockingStub( this.channel);
    }

    public void shutdown() throws InterruptedException{
        System.out.println("Chiusura canale...");
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Canale chiuso ");
    }

    public void createFilm(Film film){
        FilmDTO request = FilmMapper.toGrpc(film);

        OperationResponse response = blockingStub.create(request);

        if(!response.getValid())
            throw new RuntimeException(response.getMessage());

        System.out.println(response.getMessage());
    }


    public List<Film> searchFilms(){
        SearchFilmRequest request = SearchFilmRequest.newBuilder().build();

        FilmListResponse response = blockingStub.searchFilms(request);
        List<FilmDTO> listDTOs = response.getFilmListList();

        List<Film> listFilm = new ArrayList<>();

        for (FilmDTO filmDTO : listDTOs){
            listFilm.add(FilmMapper.fromGrpc(filmDTO));
        }
        return listFilm;
    }

    public List<Film> searchFilms(FilmFilter filter){

        SearchFilmRequest searchFilmRequest = createFilmFilter(filter);

        FilmListResponse response = blockingStub.searchFilms(searchFilmRequest);
        List<FilmDTO> listDTOs = response.getFilmListList();

        List<Film> listFilm = new ArrayList<>();

        for (FilmDTO filmDTO : listDTOs){
            listFilm.add(FilmMapper.fromGrpc(filmDTO));
        }
        return listFilm;
    }

    public void update(Film film){
        FilmDTO request = FilmMapper.toGrpc(film);
        OperationResponse response = blockingStub.update(request);
        if(!response.getValid())
            throw new RuntimeException(response.getMessage());
        System.out.println(response.getMessage());
    }

    public void delete(Integer id){
        FilmIdRequest filmIdRequest = FilmIdRequest.newBuilder().setId(id).build();
        OperationResponse response = blockingStub.delete(filmIdRequest);
        if(!response.getValid())
            throw new RuntimeException(response.getMessage());
        System.out.println(response.getMessage());
    }

    private SearchFilmRequest createFilmFilter(FilmFilter filter){
        ViewingStatusDTO viewingStatusDTO = ViewingStatusMapper.toGrpc(filter.getViewingStatus());
        SortByDTO sortByDTO = SortByMapper.toGrpc(filter.getSortBy());

        SearchFilmRequest.Builder searchFilmRequest = SearchFilmRequest.newBuilder();

        if(filter.getTitle() != null && !filter.getTitle().isEmpty())
            searchFilmRequest.setTitle(filter.getTitle());
        if(filter.getDirector() != null && !filter.getDirector().isEmpty())
            searchFilmRequest.setDirector(filter.getDirector());
        if(filter.getGenre() != null && !filter.getGenre().isEmpty())
            searchFilmRequest.setGenre(filter.getGenre());
        if(filter.getYearOfRelease()!=null && !(filter.getYearOfRelease()<1895))
            searchFilmRequest.setYearOfRelease(filter.getYearOfRelease());
        if(filter.getViewingStatus() != null && !filter.getViewingStatus().equals(ViewingStatus.UNKNOWN_STATUS))
            searchFilmRequest.setViewingStatus(viewingStatusDTO);
        if(filter.getSortBy()!=null && !filter.getSortBy().equals(SortBy.NONE)) {
            searchFilmRequest.setSortBy(sortByDTO);
            searchFilmRequest.setSortAscending(filter.getSortDirection());
        }

        return searchFilmRequest.build();
    }
}
