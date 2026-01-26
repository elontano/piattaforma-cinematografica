package it.unical.dimes.controller;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.unical.dimes.entities.Film;
import it.unical.dimes.exception.ValidationException;
import it.unical.dimes.protocol.*;
import it.unical.dimes.services.FilmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Film Grpc Connection Tests")
class FilmGrpcControllerTest {

    @Mock
    private FilmService filmService;

    @Mock
    private StreamObserver<FilmDTO> responseObserver;
    @Mock
    private StreamObserver<FilmListResponse> listResponseStreamObserver;
    @Mock
    private StreamObserver<OperationResponse> operationResponseStreamObserver;

    @InjectMocks
    private FilmGrpcController filmGrpcController;

    @Test
    @DisplayName("Should create film")
    void shouldCreateFilm(){
        CreateFilmRequest request = CreateFilmRequest.newBuilder()
                .setUserId(1)
                .setTitle("Avatar")
                .build();

        Film savedFilm = new Film.Builder("Avatar").id(10).build();

        when(filmService.save(any(Film.class),eq(1))).thenReturn(savedFilm);

        filmGrpcController.create(request,responseObserver);

        ArgumentCaptor<FilmDTO> captor = ArgumentCaptor.forClass(FilmDTO.class);
        verify(responseObserver).onNext(captor.capture());

        FilmDTO response = captor.getValue();
        assertEquals(10,response.getId());
        assertEquals("Avatar",response.getTitle());

        verify(responseObserver).onCompleted();
        verify(responseObserver,never()).onError(any());
    }

    @Test
    @DisplayName("Should return onError when service fails")
    void shouldReturnError(){
        CreateFilmRequest request = CreateFilmRequest.newBuilder().build();

        doThrow(new ValidationException("")).when(filmService).save(any(),anyInt());

        filmGrpcController.create(request,responseObserver);

        verify(responseObserver).onError(isA(StatusRuntimeException.class));

        verify(responseObserver,never()).onNext(any());
        verify(responseObserver,never()).onCompleted();
    }

    @Test
    @DisplayName("Should search films ")
    void shouldSearchFilms(){
        SearchFilmRequest request  = SearchFilmRequest.newBuilder().setUserId(1).setTitle("Avatar").build();

        List<Film> films = List.of(new Film.Builder("Avatar").build());
        when(filmService.search(any(),eq(1))).thenReturn(films);

        filmGrpcController.searchFilms(request,listResponseStreamObserver);

        ArgumentCaptor<FilmListResponse> captor = ArgumentCaptor.forClass(FilmListResponse.class);
        verify(listResponseStreamObserver).onNext(captor.capture());

        FilmListResponse response = captor.getValue();
        assertEquals(1,response.getFilmListCount());
        assertEquals("Avatar",response.getFilmList(0).getTitle());

        verify(listResponseStreamObserver).onCompleted();
    }

    @Test
    @DisplayName("Should update film")
    void shouldUpdateFilm(){
        FilmDTO request = FilmDTO.newBuilder().setId(1).setTitle("Avatar").setUserId(1).build();

        filmGrpcController.update(request,operationResponseStreamObserver);

        ArgumentCaptor<OperationResponse> captor = ArgumentCaptor.forClass(OperationResponse.class);
        verify(operationResponseStreamObserver).onNext(captor.capture());
        assertTrue(captor.getValue().getValid());
        verify(operationResponseStreamObserver).onCompleted();
    }

    @Test
    @DisplayName("Should delete film")
    void shouldDeleteFilm(){
        FilmIdRequest request = FilmIdRequest.newBuilder().setId(1).setUserId(1).build();

        filmGrpcController.delete(request,operationResponseStreamObserver);

        ArgumentCaptor<OperationResponse> captor = ArgumentCaptor.forClass(OperationResponse.class);
        verify(operationResponseStreamObserver).onNext(captor.capture());

        assertTrue(captor.getValue().getValid());
        verify(operationResponseStreamObserver).onCompleted();

        verify(filmService).delete(1,1);
    }
}