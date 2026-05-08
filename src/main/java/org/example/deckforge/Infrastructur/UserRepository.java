package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository implements IUserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, user.getUserName(), user.getEmail(), user.getPassword());
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public User findUserByEmail(String email) {
        String sql = "SELECT userId, userName, email, password, roleType " +
                "FROM users " +
                "WHERE email = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) ->
                new User(
                        rs.getInt("userId"),
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("roleType"),
                        true
                )
        );
    }
}
