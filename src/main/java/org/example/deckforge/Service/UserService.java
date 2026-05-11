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

    public int createUser(User user) throws Exception {
        try {
            user.setPassword(passwordService.hash(user.getPassword()));
            return iUserRepository.createUser(user);
        } catch (Exception ex){
            throw new Exception("Fejl ved oprettelse af bruger");
        }
    }

    public User login(User loginUser) throws Exception {
        User dbUser = iUserRepository.loginUserByEmail(loginUser.getEmail());
        if (dbUser == null) {
            throw new Exception("Forkert Email eller password");
        }

        if (!passwordService.matches(loginUser.getPassword(), dbUser.getPassword())) {
            throw new Exception("Forkert Email eller password");
        }

        dbUser.setCurrentLogin(true);
        return dbUser;
    }

    public void updateUser(User user) throws Exception {
        try {
            String password = user.getPassword();
            if (password != null && !password.startsWith("$2")) {
                user.setPassword(passwordService.hash(password));
            }
            iUserRepository.updateUser(user);
        } catch (Exception e){
            throw new Exception ("Password er ikke sikret");
        }
    }

    public void deleteUser(int userId) throws Exception {
        if (userId < 0) {
            throw new Exception ("User id kan ikke være negativ");
        }
        try {
            iUserRepository.deleteUser(userId);
        } catch (Exception e){
            throw new Exception("Fejl ved sletning af user");
        }
    }
}
