package org.example.deckforge.Service;


import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.IUserRepository;
import org.example.deckforge.Service.Validation.UserException;
import org.example.deckforge.Service.Validation.Validation;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Test
    void createUser_shouldThrowUserException_whenRepositoryFailsWithDataAccessException() throws Exception {

        // Laver fake repository
        IUserRepository userRepository = mock(IUserRepository.class);

        // Laver fake password service
        PasswordService passwordService = mock(PasswordService.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Opretter user
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.dk");
        user.setPassword("123456");

        // Password bliver hashed
        when(passwordService.hash("123456")).thenReturn("hashedPassword");

        // Repository kaster DataAccessException
        when(userRepository.createUser(user))
                .thenThrow(new DataAccessResourceFailureException("DB fejl"));

        // Tester at UserException bliver kastet
        UserException exception = assertThrows(UserException.class, () -> {
            userService.createUser(user);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved oprettelse af bruger", exception.getMessage());

        // Tjekker at repository blev forsøgt kaldt
        verify(userRepository, times(1)).createUser(user);
    }

    @Test
    void login_shouldReturnUser_whenLoginIsCorrect() throws Exception {

        // Laver fake repository
        IUserRepository userRepository = mock(IUserRepository.class);

        // Laver fake password service
        PasswordService passwordService = mock(PasswordService.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Opretter loginUser, altså det brugeren indtaster
        User loginUser = new User();
        loginUser.setEmail("test@test.dk");
        loginUser.setPassword("123456");

        // Opretter dbUser, altså brugeren fra databasen
        User dbUser = new User();
        dbUser.setUserId(1);
        dbUser.setEmail("test@test.dk");
        dbUser.setPassword("hashedPassword");

        // Siger at repository skal returnere dbUser
        when(userRepository.loginUserByEmail("test@test.dk")).thenReturn(dbUser);

        // Siger at password matcher
        when(passwordService.matches("123456", "hashedPassword")).thenReturn(true);

        // Kalder login
        User result = userService.login(loginUser);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateLoginUser(loginUser);

        // Tjekker at repository søgte på email
        verify(userRepository, times(1)).loginUserByEmail("test@test.dk");

        // Tjekker at resultatet ikke er null
        assertNotNull(result);

        // Tjekker at brugeren er logget ind
        assertTrue(result.isCurrentLogin());

        // Tjekker at lastOnline er sat
        assertNotNull(result.getLastOnline());
    }

    @Test
    void login_shouldThrowException_whenPasswordIsWrong() throws Exception {

        // Laver fake repository
        IUserRepository userRepository = mock(IUserRepository.class);

        // Laver fake password service
        PasswordService passwordService = mock(PasswordService.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Opretter loginUser
        User loginUser = new User();
        loginUser.setEmail("test@test.dk");
        loginUser.setPassword("wrongPassword");

        // Opretter dbUser
        User dbUser = new User();
        dbUser.setEmail("test@test.dk");
        dbUser.setPassword("hashedPassword");

        // Repository finder brugeren
        when(userRepository.loginUserByEmail("test@test.dk")).thenReturn(dbUser);

        // Password matcher ikke
        when(passwordService.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // Tjekker at login kaster RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {userService.login(loginUser);});

        // Din service omskriver fejlen til "Kritisk fejl"
        assertEquals("Kritisk fejl", exception.getMessage());
    }

    @Test
    void updateUser_shouldUpdateUser() throws Exception {

        // Laver fake repository
        IUserRepository userRepository = mock(IUserRepository.class);

        // Laver fake password service
        PasswordService passwordService = mock(PasswordService.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);
        // Opretter user
        User user = new User();
        user.setUserId(1);
        user.setUsername("test");
        user.setEmail("test@test.dk");
        user.setPassword("newPassword");

        // Når password hashes, returneres fake hash
        when(passwordService.hash("newPassword")).thenReturn("hashedNewPassword");

        // Kalder update
        userService.updateUser(user);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).updateUser(user);

        // Tjekker at password blev hashed
        verify(passwordService, times(1)).hash("newPassword");

        // Tjekker at repository blev kaldt
        verify(userRepository, times(1)).updateUser(user);
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {

        // Laver fake repository
        IUserRepository userRepository = mock(IUserRepository.class);

        // Laver fake password service
        PasswordService passwordService = mock(PasswordService.class);

        // Laver fake validation
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Opretter user
        User user = new User();

        // Sætter userId
        user.setUserId(1);

        // Kalder deleteUser
        userService.deleteUser(user);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateDeleteUser(user);

        // Tjekker at repository sletter userId 1
        verify(userRepository, times(1)).deleteUser(1);
    }
}