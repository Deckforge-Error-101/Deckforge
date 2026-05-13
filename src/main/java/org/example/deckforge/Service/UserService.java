package org.example.deckforge.Service;

import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.IUserRepository;
import org.example.deckforge.Service.Validation.AuthenticationException;
import org.example.deckforge.Service.Validation.UserException;
import org.example.deckforge.Service.Validation.Validation;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final IUserRepository iUserRepository;
    private final PasswordService passwordService;
    private final Validation validation;

    public UserService(IUserRepository iUserRepository, PasswordService passwordService, Validation validation) {
        this.iUserRepository = iUserRepository;
        this.passwordService = passwordService;
        this.validation = validation;
    }

    public int createUser(User user) {
        //Validering
        validation.validateCreateUser(user);

        //Forretningslogik
        user.setPassword(passwordService.hash(user.getPassword()));

        try {
            return iUserRepository.createUser(user);
        } catch (DataAccessException dae){
            throw new UserException("Fejl ved oprettelse af bruger");
        } catch (Exception ex){
            throw new RuntimeException("Kritisk fejl");
        }
    }

    public User login(User loginUser) {
        //Validering
        validation.validateLoginUser(loginUser);

        try {
            User dbUser = iUserRepository.loginUserByEmail(loginUser.getEmail());

            if (dbUser == null) {
                throw new AuthenticationException("Forkert Email eller password");
            }

            //Databasen tjekker passwords op mod hinanden.
            if (!passwordService.matches(loginUser.getPassword(), dbUser.getPassword())) {
                throw new AuthenticationException("Forkert Email eller password");
            }

            dbUser.setCurrentLogin(true);
            dbUser.setLastOnline(LocalDateTime.now());
            return dbUser;

        } catch (DataAccessException dae){
            throw new UserException("Fejl ved login");
        } catch (Exception ex){
            throw new RuntimeException("Kritisk fejl");
        }
    }

    public void updateUser(User user) {
        //Valdering
        validation.updateUser(user);
        try {

            String password = user.getPassword();
            if (password != null && !password.startsWith("$2")) {
                user.setPassword(passwordService.hash(password));
            }

            iUserRepository.updateUser(user);
        } catch (DataAccessException dae){
            throw new UserException("Fejl ved opdatering af bruger");
        } catch (Exception ex){
            throw new RuntimeException("Kritisk fejl");
        }
    }

    public void deleteUser(User user) {
        //Validering
        validation.validateDeleteUser(user);

        try {
            iUserRepository.deleteUser(user.getUserId());
        } catch (DataAccessException dae){
            throw new UserException("Fejl ved sletning af user");
        } catch (Exception ex){
            throw new RuntimeException("Kritisk fejl");
        }
    }
}
