package org.example.deckforge.Service;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordServiceTest {

    // Test af hash()
    @Test
    void hash_shouldReturnHashedPassword() {

        // Opretter service
        PasswordService passwordService = new PasswordService();

        // Laver rå password
        String rawPassword = "test123";

        // Kalder metoden
        String hashedPassword = passwordService.hash(rawPassword);

        // Tjekker at hashed password ikke er null
        assertNotNull(hashedPassword);

        // Tjekker at hashed password ikke er det samme som rå password
        assertNotEquals(rawPassword, hashedPassword);

        // Tjekker at password er BCrypt-format
        assertTrue(hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$"));
    }

    // Test af matches() med korrekt password
    @Test
    void matches_shouldReturnTrue_whenPasswordMatches() {

        // Opretter service
        PasswordService passwordService = new PasswordService();

        // Laver rå password
        String rawPassword = "test123";

        // Hasher password
        String hashedPassword = passwordService.hash(rawPassword);

        // Kalder metoden
        boolean result = passwordService.matches(rawPassword, hashedPassword);

        // Tjekker at password matcher
        assertTrue(result);
    }

    // Test af matches() med forkert password
    @Test
    void matches_shouldReturnFalse_whenPasswordDoesNotMatch() {

        // Opretter service
        PasswordService passwordService = new PasswordService();

        // Laver rå password
        String rawPassword = "test123";

        // Hasher password
        String hashedPassword = passwordService.hash(rawPassword);

        // Kalder metoden med forkert password
        boolean result = passwordService.matches("forkertPassword", hashedPassword);

        // Tjekker at password ikke matcher
        assertFalse(result);
    }

    // Test af hash() med null password
    @Test
    void hash_shouldThrowRuntimeException_whenPasswordIsNull() {

        // Opretter service
        PasswordService passwordService = new PasswordService();

        // Tjekker at exception bliver kastet
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            passwordService.hash(null);
        });

        // Tjekker fejlbeskeden
        assertEquals("Kritisk fejl, kontakt administrator", exception.getMessage());
    }

    // Test af matches() med ugyldigt encoded password
    @Test
    void matches_shouldReturnFalse_whenEncodedPasswordIsInvalid() {

        // Opretter service
        PasswordService passwordService = new PasswordService();

        // Kalder metoden med ugyldigt hash
        boolean result = passwordService.matches("test123", "ikkeEtRigtigtHash");

        // Tjekker at password ikke matcher
        assertFalse(result);
    }
}