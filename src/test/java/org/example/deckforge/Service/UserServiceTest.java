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

    // Test af createUser()
    @Test
    void createUser_shouldHashPasswordAndCallRepository() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer password
        when(user.getPassword()).thenReturn("test123");
        when(passwordService.hash("test123")).thenReturn("hashedPassword");

        // Bestemmer hvad repository skal returnere
        when(userRepository.createUser(user)).thenReturn(1);

        // Kalder metoden
        int result = userService.createUser(user);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateCreateUser(user);

        // Tjekker at password blev hashed
        verify(passwordService, times(1)).hash("test123");

        // Tjekker at det hashed password blev sat på user
        verify(user, times(1)).setPassword("hashedPassword");

        // Tjekker at repository blev kaldt
        verify(userRepository, times(1)).createUser(user);

        // Tjekker at userId bliver returneret
        assertEquals(1, result);
    }

    // Test af createUser() ved databasefejl
    @Test
    void createUser_shouldThrowUserException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer password
        when(user.getPassword()).thenReturn("test123");
        when(passwordService.hash("test123")).thenReturn("hashedPassword");

        // Simulerer databasefejl
        when(userRepository.createUser(user)).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at UserException bliver kastet
        UserException exception = assertThrows(UserException.class, () -> {
            userService.createUser(user);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kunne ikke oprette brugeren, prøv igen senere", exception.getMessage());
    }

    // Test af login()
    @Test
    void login_shouldReturnUser_whenLoginIsCorrect() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock users
        User loginUser = mock(User.class);
        User dbUser = mock(User.class);

        // Simulerer login-data
        when(loginUser.getEmail()).thenReturn("test@mail.com");
        when(loginUser.getPassword()).thenReturn("test123");
        when(dbUser.getPassword()).thenReturn("hashedPassword");

        // Bestemmer hvad repository skal returnere
        when(userRepository.loginUserByEmail("test@mail.com")).thenReturn(dbUser);

        // Simulerer at password matcher
        when(passwordService.matches("test123", "hashedPassword")).thenReturn(true);

        // Kalder metoden
        User result = userService.login(loginUser);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateLoginUser(loginUser);

        // Tjekker at bruger bliver fundet via email
        verify(userRepository, times(1)).loginUserByEmail("test@mail.com");

        // Tjekker at password bliver sammenlignet
        verify(passwordService, times(1)).matches("test123", "hashedPassword");

        // Tjekker at currentLogin bliver sat til true
        verify(dbUser, times(1)).setCurrentLogin(true);

        // Tjekker at lastOnline bliver sat
        verify(dbUser, times(1)).setLastOnline(any());

        // Tjekker at dbUser bliver returneret
        assertEquals(dbUser, result);
    }

    // Test af login() når bruger ikke findes
    @Test
    void login_shouldThrowRuntimeException_whenUserDoesNotExist() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock login user
        User loginUser = mock(User.class);

        // Simulerer email
        when(loginUser.getEmail()).thenReturn("test@mail.com");

        // Simulerer at bruger ikke findes
        when(userRepository.loginUserByEmail("test@mail.com")).thenReturn(null);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(loginUser);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl", exception.getMessage());

        // Tjekker at password IKKE bliver tjekket
        verify(passwordService, never()).matches(anyString(), anyString());
    }

    // Test af login() med forkert password
    @Test
    void login_shouldThrowRuntimeException_whenPasswordIsWrong() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock users
        User loginUser = mock(User.class);
        User dbUser = mock(User.class);

        // Simulerer login-data
        when(loginUser.getEmail()).thenReturn("test@mail.com");
        when(loginUser.getPassword()).thenReturn("forkert");
        when(dbUser.getPassword()).thenReturn("hashedPassword");

        // Bestemmer hvad repository skal returnere
        when(userRepository.loginUserByEmail("test@mail.com")).thenReturn(dbUser);

        // Simulerer at password ikke matcher
        when(passwordService.matches("forkert", "hashedPassword")).thenReturn(false);

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(loginUser);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl", exception.getMessage());
    }

    // Test af login() ved databasefejl
    @Test
    void login_shouldThrowUserException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock login user
        User loginUser = mock(User.class);

        // Simulerer email
        when(loginUser.getEmail()).thenReturn("test@mail.com");

        // Simulerer databasefejl
        when(userRepository.loginUserByEmail("test@mail.com")).thenThrow(new DataAccessResourceFailureException("Database error"));

        // Tjekker at UserException bliver kastet
        UserException exception = assertThrows(UserException.class, () -> {
            userService.login(loginUser);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved login", exception.getMessage());
    }

    // Test af updateUser() med rå password
    @Test
    void updateUser_shouldHashPasswordAndCallRepository() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer rå password
        when(user.getPassword()).thenReturn("test123");
        when(passwordService.hash("test123")).thenReturn("hashedPassword");

        // Kalder metoden
        userService.updateUser(user);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).updateUser(user);

        // Tjekker at password blev hashed
        verify(passwordService, times(1)).hash("test123");

        // Tjekker at hashed password blev sat
        verify(user, times(1)).setPassword("hashedPassword");

        // Tjekker at repository blev kaldt
        verify(userRepository, times(1)).updateUser(user);
    }

    // Test af updateUser() med allerede hashed password
    @Test
    void updateUser_shouldNotHashPassword_whenPasswordIsAlreadyHashed() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer allerede hashed password
        when(user.getPassword()).thenReturn("$2a$hashedPassword");

        // Kalder metoden
        userService.updateUser(user);

        // Tjekker at password IKKE bliver hashed igen
        verify(passwordService, never()).hash(anyString());

        // Tjekker at repository blev kaldt
        verify(userRepository, times(1)).updateUser(user);
    }

    // Test af updateUser() ved databasefejl
    @Test
    void updateUser_shouldThrowUserException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer allerede hashed password
        when(user.getPassword()).thenReturn("$2a$hashedPassword");

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(userRepository).updateUser(user);

        // Tjekker at UserException bliver kastet
        UserException exception = assertThrows(UserException.class, () -> {
            userService.updateUser(user);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved opdatering af bruger", exception.getMessage());
    }

    // Test af deleteUser()
    @Test
    void deleteUser_shouldCallRepository() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer userId
        when(user.getUserId()).thenReturn(1);

        // Kalder metoden
        userService.deleteUser(user);

        // Tjekker at validation blev kaldt
        verify(validation, times(1)).validateDeleteUser(user);

        // Tjekker at repository blev kaldt
        verify(userRepository, times(1)).deleteUser(1);
    }

    // Test af deleteUser() ved databasefejl
    @Test
    void deleteUser_shouldThrowUserException_whenRepositoryFails() throws Exception {

        // Laver fake/mock repository, passwordService og validation
        IUserRepository userRepository = mock(IUserRepository.class);
        PasswordService passwordService = mock(PasswordService.class);
        Validation validation = mock(Validation.class);

        // Opretter service
        UserService userService = new UserService(userRepository, passwordService, validation);

        // Laver fake/mock user
        User user = mock(User.class);

        // Simulerer userId
        when(user.getUserId()).thenReturn(1);

        // Simulerer databasefejl
        doThrow(new DataAccessResourceFailureException("Database error")).when(userRepository).deleteUser(1);

        // Tjekker at UserException bliver kastet
        UserException exception = assertThrows(UserException.class, () -> {
            userService.deleteUser(user);
        });

        // Tjekker fejlbeskeden
        assertEquals("Fejl ved sletning af user", exception.getMessage());
    }
}