package it.unical.dimes.services;

import it.unical.dimes.entities.Film;
import it.unical.dimes.entities.FilmFilter;
import it.unical.dimes.exceptions.FilmNotFoundException;
import it.unical.dimes.exceptions.ValidationException;
import it.unical.dimes.repositories.FilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private Film filmNull;
    private Film.Builder invalidFilmBuilder;
    private Film invalidFilmYear;
    private Film invalidFilmTitleEmpty;

    private int userId;
    private Integer userIdNull;

    @BeforeEach
    void setUp(){
        userId = 1;
        userIdNull = null;

        validFilm = new Film.Builder("Inception")
                .id(1)
                .rating(5)
                .yearOfRelease(2010)
                .build();

        filmNull = null;

        invalidFilmBuilder = new Film.Builder("Invalid Film");

        invalidFilmYear = new Film.Builder("Avatar")
                .yearOfRelease(1800)
                .build();

        invalidFilmTitleEmpty = new Film.Builder("").build();
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save successfully when valid id and film is provided")
        void shouldSaveSuccessfully(){
            when(filmRepository.save(validFilm,userId)).thenReturn(validFilm);

            Film savedFilm = filmService.save(validFilm,userId);

            assertNotNull(savedFilm,"Il film salvato non dovrebbe essere null");
            assertEquals("Inception",savedFilm.getTitle());
        }

        @Test
        @DisplayName("Should throws ValidationException: user id cannot be null")
        void shouldThrowsValidationExceptionIdNull(){
            assertValidationFails(()->filmService.save(validFilm,userIdNull));
        }

        @Test
        @DisplayName("Should throws ValidationException: film data cannot nulla")
        void shouldThrowsValidationExceptionFilmNull(){
            assertValidationFails(()-> filmService.save(filmNull,userId));
        }

        @Test
        @DisplayName("Should throws ValidationException: year not valid")
        void shouldThrowsValidationExceptionYearNotValid(){
            assertValidationFails(()->filmService.save(invalidFilmYear,userId));
        }

        @Test
        @DisplayName("Should throws ValidationException: title is empty")
        void shouldThrowsValidationExceptionTitleNotValid(){
            assertValidationFails(()->filmService.save(invalidFilmTitleEmpty,userId));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1,6})
        @DisplayName("Should throws ValidationException: rating out of rage")
        void shouldThrowsValidationExceptionRatingOutOfRange(int invalidRating){
            Film invalidFilm = invalidFilmBuilder.rating(invalidRating).build();
            assertValidationFails(() -> filmService.save(invalidFilm, userId));
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTest{

        @Test
        @DisplayName("Should update successfully")
        void shouldUpdateSuccessfully(){

            when(filmRepository.update(validFilm,userId)).thenReturn(true);

            assertDoesNotThrow(()->filmService.update(validFilm,userId));

            verify(filmRepository).update(validFilm,userId);
        }

        @Test
        @DisplayName("Should throws FilmNotFoundException when film not found")
        void shouldThrowsFilmNotFoundException(){

            when(filmRepository.update(validFilm,userId)).thenReturn(false);

            assertThrows(FilmNotFoundException.class, ()->filmService.update(validFilm,userId));
        }

        @Test
        @DisplayName("Should throws ValidationException: user id cannot be null")
        void shouldThrowsValidationExceptionIdNull(){
            assertValidationFails(()->filmService.update(validFilm,userIdNull));
        }

        @Test
        @DisplayName("Should throws ValidationException: film data cannot nulla")
        void shouldThrowsValidationExceptionFilmNull(){
            assertValidationFails(()-> filmService.update(filmNull,userId));
        }

        @Test
        @DisplayName("Should throws ValidationException: year not valid")
        void shouldThrowsValidationExceptionYearNotValid(){
            assertValidationFails(()->filmService.update(invalidFilmYear,userId));
        }

        @Test
        @DisplayName("Should throws ValidationException: title is empty")
        void shouldThrowsValidationExceptionTitleNotValid(){
            assertValidationFails(()->filmService.update(invalidFilmTitleEmpty,userId));
        }

        @ParameterizedTest
        @ValueSource(ints = {-1,6})
        @DisplayName("Should throws ValidationException: rating out of rage")
        void shouldThrowsValidationExceptionRatingOutOfRange(int invalidRating){
            Film invalidFilm = invalidFilmBuilder.rating(invalidRating).build();
            assertValidationFails(() -> filmService.update(invalidFilm, userId));
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTest {

        @Test
        @DisplayName("Should delete successfully")
        void shouldDeleteSuccessfully(){
            when(filmRepository.delete(validFilm.getId(),userId)).thenReturn(true);
            assertDoesNotThrow(()->filmService.delete(validFilm.getId(),userId));
            verify(filmRepository).delete(validFilm.getId(),userId);
        }

        @Test
        @DisplayName("Should throws FilmNotFoundException")
        void shouldThrowsFilmNotFoundException(){
            when(filmRepository.delete(validFilm.getId(),userId)).thenReturn(false);
            assertThrows(FilmNotFoundException.class,()->filmService.delete(validFilm.getId(),userId));
        }

        @Test
        @DisplayName("Should throws ValidationException: user id cannot be null")
        void shouldThrowsValidationExceptioId(){
            assertValidationFails(()->filmService.delete(validFilm.getId(),userIdNull));
        }
    }

    @Nested
    @DisplayName("Search Tests")
    class SearchTest{

        private FilmFilter filmFilter;

        @BeforeEach
        void setUp(){
            filmFilter = new FilmFilter.Builder()
                    .title("Inception")
                    .build();
        }

        @Test
        @DisplayName("Should return list of film when found")
        void shouldReturnFilmsWhenFound(){
            List<Film> films = List.of(validFilm);

            when(filmRepository.search(filmFilter,userId)).thenReturn(films);

            List<Film> result = filmService.search(filmFilter,userId);

            assertNotNull(result);
            assertEquals(1,result.size());
            assertEquals("Inception",result.get(0).getTitle());
            verify(filmRepository).search(filmFilter,userId);

        }

        @Test
        @DisplayName("Should return empyt list when film not found")
        void shouldReturnEmptyListWhenFilmNotFound(){
            when(filmRepository.search(filmFilter,userId)).thenReturn(Collections.emptyList());

            List<Film> result = filmService.search(filmFilter,userId);

            assertNotNull(result);
            assertEquals(0,result.size());

            verify(filmRepository).search(filmFilter,userId);
        }

        @Test
        @DisplayName("Should throws ValidationException: user id cannot be null")
        void shouldThrowsValidationExceptionIdNull(){
            assertValidationFails(()->filmService.search(filmFilter,userIdNull));
        }
    }

     private void assertValidationFails(Executable operation){
        assertThrows(ValidationException.class,operation);
        //nessuna interazione (save,update, ecc)
        verifyNoInteractions(filmRepository);
    }
}