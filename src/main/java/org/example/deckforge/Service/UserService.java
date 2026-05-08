package org.example.deckforge.Service;

import org.example.deckforge.Domain.User;
import org.example.deckforge.Infrastructur.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final IUserRepository iUserRepository;
    private final PasswordService passwordService;

    public UserService(IUserRepository iUserRepository, PasswordService passwordService) {
        this.iUserRepository = iUserRepository;
        this.passwordService = passwordService;
    }

    public void createUser(User user) {
        try {
            user.setPassword(passwordService.hash(user.getPassword()));
            iUserRepository.createUser(user);
        } catch (Exception e) {
        }
    }

    public User login(User loginUser) {
        User dbUser;
        try {
            dbUser = iUserRepository.findUserByEmail(loginUser.getEmail());
        } catch (Exception e) {

        }

        if (!passwordService.matches(loginUser.getPassword(), dbUser.getPassword())) {
            throw new Exception("Forkert Email eller password");
        }

        dbUser.setCurrentLogin(true);
        return dbUser;
    }
}
