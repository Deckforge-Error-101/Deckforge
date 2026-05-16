package org.example.deckforge.Service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordServiceTest {

    // Test af hash()
    @Test
    void hash_shouldReturnEncodedPassword() {

        // Opretter PasswordService
        PasswordService passwordService = new PasswordService();

        // Gemmer original password
        String rawPassword = "123456";

        // Hasher passwordet
        String hashedPassword = passwordService.hash(rawPassword);

        // Tjekker at hashed password ikke er null
        assertNotNull(hashedPassword);

        // Tjekker at hashed password IKKE er det samme som original password
        assertNotEquals(rawPassword, hashedPassword);
    }

    // Test af matches() når password passer
    @Test
    void matches_shouldReturnTrue_whenPasswordMatches() {

        // Opretter PasswordService
        PasswordService passwordService = new PasswordService();

        // Original password
        String rawPassword = "mypassword";

        // Hasher passwordet
        String hashedPassword = passwordService.hash(rawPassword);

        // Tjekker om password matcher hash
        boolean result = passwordService.matches(rawPassword, hashedPassword);

        // Tjekker at resultatet er true
        assertTrue(result);
    }

    // Test af matches() når password IKKE passer
    @Test
    void matches_shouldReturnFalse_whenPasswordDoesNotMatch() {

        // Opretter PasswordService
        PasswordService passwordService = new PasswordService();

        // Rigtigt password
        String rawPassword = "mypassword";

        // Forkert password
        String wrongPassword = "wrongpassword";

        // Hasher det rigtige password
        String hashedPassword = passwordService.hash(rawPassword);

        // Tjekker om forkert password matcher hash
        boolean result = passwordService.matches(wrongPassword, hashedPassword);

        // Tjekker at resultatet er false
        assertFalse(result);
    }
}