package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.CardToTrade;
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

    @Override
    public int getQuantityOwned(int userId, int cardId) {
        String sql = "SELECT quantity FROM collections WHERE userid = ? AND cardid = ?";

        List<Integer> results = jdbcTemplate.query(sql, new Object[]{userId, cardId}, (rs, rowNum) ->
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
                FROM collections col
                JOIN cards c ON col.cardId = c.cardId
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
                UPDATE collections SET quantity = quantity - 1 WHERE userId = ? AND cardId = ?
                """;
        jdbcTemplate.update(sql, userId, cardId);

        String deleteSql = """
                DELETE FROM collections WHERE userId = ? AND cardId = ?
                """;
        jdbcTemplate.update(deleteSql, userId, cardId);
    }

    @Override
    public void addCardToCollection(int userId, int cardId){
        String sql = """
                INSERT INTO collections (userId, cardId, quantity) VALUES (?, ?, 1)
                ON DUPLICATE KEY UPDATE quantity = quantity + 1
                """;
        jdbcTemplate.update(sql, userId, cardId);
    }

    @Override
    public void updateTradeId(int userId, int cardId, String tradeId){
        String sql = """
                UPDATE collections SET tradeId = ? WHERE userId = ? AND cardId = ?
                """;
        jdbcTemplate.update(sql, tradeId, userId, cardId);
    }
}


