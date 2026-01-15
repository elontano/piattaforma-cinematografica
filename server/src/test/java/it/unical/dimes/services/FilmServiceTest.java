package it.unical.dimes.services;

import it.unical.dimes.entities.Film;
import it.unical.dimes.repositories.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //abilitazione uso delle annotazioni di Mockito
@DisplayName("FilmService Unit Test")
class FilmServiceTest {

    @Mock //crea una finta istanza di una classe o interfaccia, invece di usare un vero repository creiamo un mock
    private FilmRepository filmRepository;

    @InjectMocks //il pezzo da testare SUT (System Under Test)
    //crea un'istanza reale della classe e se trova dei campi che corrispondono ai mock
    //creati sopra, li inserisce dentro automaticamente
    private FilmService filmService; //la classe da testare

    private Film validFilm;

    @BeforeEach
    //metodo da eseguire prima di ogni singolo test
    void setUp(){
        validFilm = new Film.Builder("Inception")
                .rating(5)
                .yearOfRelease(2010)
                .build();
    }

    @Nested
    class CreateSaveTests{

        @Test
        @DisplayName("Should save successfully when valid id and film is provided")
        void shouldSaveSuccessfully(){
            //given
            Integer userId = 1;

            when(filmRepository.save(validFilm,userId)).thenReturn(validFilm);

            //when
            Film savedFilm = filmService.save(validFilm,userId);

            //then (verifica)
            assertNotNull(savedFilm,"Il film salvato non dovrebbe essere null");
            assertEquals("Inception",savedFilm.getTitle());
        }
    }
    //test casi di successo
    //test validazioni
    //test eccezioni
}