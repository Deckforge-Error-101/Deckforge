package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.User;

import java.util.List;

public interface IUserRepository {
    int createUser(User user);
    void updateUser(User user);
    void deleteUser(int userId);
    User loginUserByEmail(String email);
}
