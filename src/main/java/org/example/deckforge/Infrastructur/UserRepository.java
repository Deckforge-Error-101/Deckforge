package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.User;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepository implements IUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUser(User user) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }
}
