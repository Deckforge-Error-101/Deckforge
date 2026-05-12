package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Collection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
        String sql = "INSERT INTO collections (userid, cardid, tradeid, quantity) VALUES (?, ?, ?, 1)" +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";
        jdbcTemplate.update(sql, userId, cardId, tradeId);
    }

    @Override
    public void deleteCardFromCollection(int userId, int cardId) {
        String checkSql = "SELECT quantity FROM collections WHERE userid = ? AND cardid = ?";
        Integer currentQuantity = jdbcTemplate.queryForObject(checkSql, Integer.class, userId, cardId);

        if (currentQuantity != null) {
            if (currentQuantity > 1) {
                String updateSql = "UPDATE collections SET quantity = quantity - 1 WHERE userid = ? AND cardid = ?";
                jdbcTemplate.update(updateSql, userId, cardId);
            } else {
                String deleteSql = "DELETE FROM collections WHERE userid = ? AND cardid = ?";
                jdbcTemplate.update(deleteSql, userId, cardId);
            }
        }
    }

    @Override
    public List<Card> findUserCollection(int userId) {
        String sql = """
            SELECT c.cardid, c.cardname, c.typeid, c.rarity, col.quantity
            FROM collections col
            JOIN cards c ON col.cardid = c.cardid
            WHERE col.userid = ?
            """;

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new Card(
                        rs.getInt("cardid"),
                        rs.getString("cardname"),
                        rs.getString("typeid"),
                        rs.getString("rarity"),
                        rs.getString("quantity")
                )
        );
    }
}
