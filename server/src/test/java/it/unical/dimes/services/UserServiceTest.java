package it.unical.dimes.services;

import it.unical.dimes.entities.User;
import it.unical.dimes.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Unit Test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private User userPasswordNull;
    private User expectedUser;

    @BeforeEach
    void setUp(){
        user = new User("test","password");
        userPasswordNull = new User("testNoPass",null);
        expectedUser = new User(1,"test","password");
    }

    @Nested
    @DisplayName("Registration Tests")
    class RegistrationTests{

        @Test
        @DisplayName("Should register successfully")
        void shouldRegisterSuccessfully(){
            //simuliamo che il db restituisca l'utente che gli abbiamo passato
            //thenAnswer è dinamico rispeto a thenReturn
            //quando ti chiamano con qualsiasi utente esegui la funzione
            when(userRepository.save(any(User.class))).thenAnswer(
                    invocation -> invocation.getArgument(0)
            );

            User registeredUser = userService.registerUser(user);

            assertNotNull(registeredUser);
            assertEquals("test",registeredUser.getUsername());
            assertNotEquals(user.getPassword(),registeredUser.getUsername());

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(captor.capture());
            User captured = captor.getValue();
            assertEquals("test",captured.getUsername());
            assertNotEquals("password",captured.getPassword());
        }

        @Test
        @DisplayName("Should throws IllegalArgumentException: password null ")
        void shouldThrowsIllegalArgumentExceptionPasswordNull(){

            assertThrows(IllegalArgumentException.class,
                    ()->userService.registerUser(userPasswordNull));

            verifyNoInteractions(userRepository);
        }
    }

    @Nested
    @DisplayName("Find By Username Test")
    class FindByUsername{

        @Test
        @DisplayName("Should return user when found")
        void shouldReturnUserWhenFound(){
            when(userRepository.findByUsername("test")).thenReturn(expectedUser);

            User foundUser = userService.findByUsername("test");

            assertNotNull(foundUser);
            assertEquals("test",foundUser.getUsername());
            verify(userRepository).findByUsername("test");
        }

        @Test
        @DisplayName("Should not return user when not found")
        void shouldNotReturnWhenNotFound(){
            when(userRepository.findByUsername("test")).thenReturn(null);

            User foundUser = userService.findByUsername("test");

            assertNull(foundUser);
            verify(userRepository).findByUsername("test");
        }
    }
}