package org.example.deckforge.Service;

import org.example.deckforge.Service.Validation.UserException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hash(String rawPassword) {
        try {
            return encoder.encode(rawPassword);
        } catch (DataAccessException dae) {
            throw new UserException("Der er sket en fejl ved password, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            return encoder.matches(rawPassword, encodedPassword);
        } catch (DataAccessException dae) {
            throw new UserException("Der er sket en fejl ved password, prøv igen senere");
        } catch (Exception ex) {
            throw new RuntimeException("Kritisk fejl, kontakt administrator");
        }
    }
}