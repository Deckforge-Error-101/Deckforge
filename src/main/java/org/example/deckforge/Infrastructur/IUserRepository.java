package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.User;

public interface IUserRepository {
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
}
