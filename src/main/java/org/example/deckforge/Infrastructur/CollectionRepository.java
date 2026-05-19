package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
import org.example.deckforge.Domain.Collection;
import org.example.deckforge.Domain.User;
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
        String sql = "INSERT INTO Collections (userId, tradeId, cardId) VALUES (?, NULL, NULL)";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void addCardToCollection(int userId, int cardId, String tradeId) {
        String sql = "INSERT INTO Collections (userId, cardId, tradeId, quantity) VALUES (?, ?, ?, 1)" +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";
        jdbcTemplate.update(sql, userId, cardId, tradeId);
    }

    @Override
    public void deleteCardFromCollection(int userId, int cardId) {
        String checkSql = "SELECT quantity FROM Collections WHERE userId = ? AND cardId = ?";
        Integer currentQuantity = jdbcTemplate.queryForObject(checkSql, Integer.class, userId, cardId);

        if (currentQuantity != null) {
            if (currentQuantity > 1) {
                String updateSql = "UPDATE Collections SET quantity = quantity - 1 WHERE userId = ? AND cardId = ?";
                jdbcTemplate.update(updateSql, userId, cardId);
            } else {
                String deleteSql = "DELETE FROM Collections WHERE userId = ? AND cardId = ?";
                jdbcTemplate.update(deleteSql, userId, cardId);
            }
        }
    }

    @Override
    public List<Card> findUserCollection(User user) {
        String sql = """
                SELECT c.cardId, c.cardName, c.typeId, c.rarity, col.quantity, col.tradeId
                FROM Collections col
                JOIN Cards c ON col.cardId = c.cardId
                WHERE col.userId = ?
                """;

        return jdbcTemplate.query(sql, new Object[]{user.getUserId()}, (rs, rowNum) ->
                new Card(
                        rs.getInt("cardId"),
                        rs.getString("cardName"),
                        rs.getString("typeId"),
                        rs.getString("rarity"),
                        rs.getInt("quantity"),
                        rs.getString("tradeId")
                )
        );
    }


    @Override
    public int getQuantityOwned(User user, Card card) {
        String sql = "SELECT quantity FROM Collections WHERE userId = ? AND cardId = ?";

        List<Integer> results = jdbcTemplate.query(sql, new Object[]{user.getUserId(), card.getCardId()}, (rs, rowNum) ->
                rs.getInt("quantity")
        );

        //En smart måde at undgå NullPointException.
        //Hvis listen er tom retuner 0 hvis ikke retuner værdien af listen.
        if (results.isEmpty()) {
            return 0;
        } else {
            return results.get(0);
        }
    }

    @Override
    public CardToTrade findCardToTrade(String tradeId) {
        String sql = """
                SELECT col.userId, col.cardId, c.cardName
                FROM Collections col
                JOIN Cards c ON col.cardId = c.cardId
                WHERE col.tradeId = ?
                """;

        List<CardToTrade> results = jdbcTemplate.query(sql, new Object[]{tradeId}, (rs, rowNum) ->
                new CardToTrade(
                        rs.getInt("userId"),
                        rs.getInt("cardId"),
                        rs.getString("cardName")
                )
        );
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    @Override
    public void removeCardFromCollection(int userId, int cardId){
        String sql = """
                UPDATE Collections SET quantity = quantity - 1 WHERE userId = ? AND cardId = ?
                """;
        jdbcTemplate.update(sql, userId, cardId);

        String deleteSql = """
                DELETE FROM Collections WHERE userId = ? AND cardId = ?
                """;
        jdbcTemplate.update(deleteSql, userId, cardId);
    }

    @Override
    public void addCardToCollection(int userId, int cardId){
        String sql = """
                INSERT INTO Collections (userId, cardId, quantity) VALUES (?, ?, 1)
                ON DUPLICATE KEY UPDATE quantity = quantity + 1
                """;
        jdbcTemplate.update(sql, userId, cardId);
    }

    @Override
    public void updateTradeId(int userId, int cardId, String tradeId){
        String sql = """
                UPDATE Collections SET tradeId = ? WHERE userId = ? AND cardId = ?
                """;
        jdbcTemplate.update(sql, tradeId, userId, cardId);
    }

    @Override
    public List<CardToTrade> findAllAvailableTrades() {
        String sql = """
                SELECT col.tradeId, u.username, col.userId, col.cardId, c.cardname, c.rarity
                FROM Collections col
                JOIN Cards c ON col.cardId = c.cardId
                JOIN Users u ON col.userId = u.userId
                WHERE col.tradeId IS NOT NULL
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new CardToTrade(
                        rs.getString("tradeId"),
                        rs.getString("username"),
                        rs.getInt("userId"),
                        rs.getInt("cardId"),
                        rs.getString("cardName"),
                        rs.getString("rarity")
                )
        );
    }
}


