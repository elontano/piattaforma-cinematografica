package it.unical.dimes.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.unical.dimes.entities.User;
import it.unical.dimes.exception.UserAlreadyExistsException;
import it.unical.dimes.protocol.UserRequest;
import it.unical.dimes.protocol.UserResponse;
import it.unical.dimes.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Grpc Connection Test")
class UserGrpcControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private StreamObserver<UserResponse> responseStreamObserver;

    @InjectMocks
    private UserGrpcController userGrpcController;

    @Nested
    @DisplayName("Register tests")
    class RegisterTests{

        @Test
        @DisplayName("Should register successfully")
        void shouldRegisterSuccessfully () {

            UserRequest request = UserRequest.newBuilder().setUsername("test").setPassword("password").build();

            User registeredUser = new User(1, "test", "password");
            when(userService.registerUser(any(User.class))).thenReturn(registeredUser);

            userGrpcController.register(request, responseStreamObserver);

            ArgumentCaptor<UserResponse> captor = ArgumentCaptor.forClass(UserResponse.class);
            verify(responseStreamObserver).onNext(captor.capture());

            UserResponse response = captor.getValue();
            assertTrue(response.getSuccess());
            assertEquals(1, response.getUserId());
            assertEquals("test", response.getUsername());

            verify(responseStreamObserver).onCompleted();
        }

        @Test
        @DisplayName("Should return error when user already exists")
        void shouldReturnAlreadyExistsException () {
            UserRequest request = UserRequest.newBuilder().setUsername("test").setPassword("password").build();

            when(userService.registerUser(any(User.class))).thenThrow(new UserAlreadyExistsException("User already exists"));

            userGrpcController.register(request, responseStreamObserver);

            ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
            verify(responseStreamObserver).onError(captor.capture());

            assertEquals(Status.ALREADY_EXISTS.getCode(),((StatusRuntimeException) captor.getValue()).getStatus().getCode());
        }
    }

    @Nested
    @DisplayName("Login tests")
    class LoginTests{

        @Test
        @DisplayName("Should Login succesfully")
        void shouldLoginSuccessfully(){

            UserRequest request = UserRequest.newBuilder().setUsername("test").setPassword("password").build();

            User user = new User(1,"test","hashedPass");
            when(userService.findByUsername("test")).thenReturn(user);
            when(userService.checkPassword("password","hashedPass")).thenReturn(true);

            userGrpcController.login(request,responseStreamObserver);

            ArgumentCaptor<UserResponse> captor = ArgumentCaptor.forClass(UserResponse.class);
            verify(responseStreamObserver).onNext(captor.capture());

            UserResponse response = captor.getValue();
            assertTrue(response.getSuccess());

            verify(responseStreamObserver).onCompleted();
        }

        @Test
        @DisplayName("Should not login successfully: Username or Password not valid")
        void shouldNotLogin(){
            UserRequest request = UserRequest.newBuilder().setUsername("test").setPassword("password").build();

            when(userService.findByUsername(anyString())).thenReturn(null);

            userGrpcController.login(request,responseStreamObserver);

            ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);
            verify(responseStreamObserver).onError(captor.capture());

            assertInstanceOf(StatusRuntimeException.class,captor.getValue());
        }

    }
}
