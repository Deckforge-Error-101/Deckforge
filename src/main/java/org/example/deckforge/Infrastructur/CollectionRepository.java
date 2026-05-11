package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Collection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionRepository implements ICollectionRepository {
    private final JdbcTemplate jdbcTemplate;

    public CollectionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createCollection(int userId) {
        String sql = "INSERT INTO collections (userid, tradeid, cardid) VALUES (?, NULL, NULL)";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void addCardToCollection(int userId, int cardId, String tradeId) {
        String sql = "INSERT INTO collections (userid, cardid, tradeid) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, cardId, tradeId);
    }

    @Override
    public void deleteCardFromCollection(int userId, int cardId) {
        String sql = "DELETE FROM collections WHERE userid = ? AND cardid = ?";
        jdbcTemplate.update(sql, userId, cardId);
    }
}
