package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.User;

public interface IUserRepository {
    int createUser(User user);
    void updateUser(User user);
    void deleteUser(int userId);
    User loginUserByEmail(String email);
}
