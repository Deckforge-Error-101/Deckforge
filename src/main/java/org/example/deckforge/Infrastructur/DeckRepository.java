package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeckRepository implements IDeckRepository {
    private final JdbcTemplate jdbcTemplate;

    public DeckRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createDeck(Deck deck) {
        String sql = "INSERT INTO decks (deckname, formatType, slots, userId) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                deck.getDeckName(),
                deck.getFormatType(),
                deck.getSlots(),
                deck.getUserId()
        );
    }

    @Override
    public List<Deck> findAllDecksByUserId(int userId) {
        String sql = "SELECT deckId, deckName, formatType, slots, is_public, userId FROM decks WHERE userId = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new Deck(
                        rs.getInt("deckId"),
                        rs.getString("deckName"),
                        rs.getString("formatType"),
                        rs.getInt("slots"),
                        rs.getBoolean("is_public"),
                        rs.getInt("userId")
                )
        );
    }

    @Override
    public void updateDeck(Deck deck) {
        String sql = "UPDATE decks SET deckName = ?, formatType = ?, slots = ? WHERE deckId = ?";

        jdbcTemplate.update(sql,
                deck.getDeckName(),
                deck.getFormatType(),
                deck.getSlots(),
                deck.getDeckId()
        );
    }

    @Override
    public void deleteDeck(int deckId) {
        String sql = "DELETE FROM decks WHERE deckId = ?";

        jdbcTemplate.update(sql, deckId);
    }

    @Override
    public Deck findDeckById(int deckId) {
        String sql = "SELECT deckId, deckName, formatType, slots, is_public, userId FROM decks WHERE deckId = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Deck(
                        rs.getInt("deckId"),
                        rs.getString("deckName"),
                        rs.getString("formatType"),
                        rs.getInt("slots"),
                        rs.getBoolean("is_public"),
                        rs.getInt("userId")
                ), deckId
        );
    }

    @Override
    public List<Card> getCardsInDeck(int deckId) {
        //Vi joiner 'cards' (c) med 'deck_cards' (dc) for at koble kort-data sammen med dæk-ID og antal
        String sql = "SELECT c.cardid, c.cardname, c.typeid, c.rarity, dc.quantity " +
                "FROM cards c " +
                "JOIN deck_cards dc ON c.cardid = dc.cardid " +
                "WHERE dc.deckid = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Card(
                        rs.getInt("cardid"),
                        rs.getString("cardname"),
                        rs.getString("typeid"),
                        rs.getString("rarity"),
                        rs.getString("quantity")
                ), deckId);
    }

    @Override
    public void addCardToDeck(int deckId, int cardId) {
        //Duplicate key så vi ikke behøver flere rækker med samme kort.
        String sql = "INSERT INTO Deck_Cards (deckId, cardId, quantity) VALUES (?, ?, 1) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";
        jdbcTemplate.update(sql, deckId, cardId);
    }

    public void removeCardFromDeck(int deckId, int cardId) {
        //Vi starter med at tjekke hvor mange af det specifikke kort der er i quantity.
        String checkSql = "SELECT quantity FROM deck_cards WHERE deckid = ? AND cardid = ?";

        try {
            //Her forventer vi at modtage en integer tilbage. 1,2,3,4,5 osv
            Integer currentQuantity = jdbcTemplate.queryForObject(checkSql, Integer.class, deckId, cardId);
            //Hvis resultatet er mindre større end 1 fjerne vi bare 1 fra quantity
            if (currentQuantity != null && currentQuantity > 1) {
                jdbcTemplate.update("UPDATE deck_cards SET quantity = quantity - 1 WHERE deckid = ? AND cardid = ?", deckId, cardId);
            } else {
                //Hvis resultatet er 1 fjerner vi hele rækken fra databasen.
                jdbcTemplate.update("DELETE FROM deck_cards WHERE deckid = ? AND cardid = ?", deckId, cardId);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void updateDeckVisibility(int deckId, boolean isPublic) {
        String sql = "UPDATE decks SET is_public = ? WHERE deckId = ?";
        jdbcTemplate.update(sql, isPublic, deckId);
    }

    @Override
    public List<Deck> findAllPublicDecks() {
        String sql = "SELECT * FROM decks WHERE is_public = true";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Deck(
                        rs.getInt("deckId"),
                        rs.getString("deckName"),
                        rs.getString("formatType"),
                        rs.getInt("slots"),
                        rs.getBoolean("is_public"),
                        rs.getInt("userId")
                )
        );
    }

    public int getQuantityInDeck(int deckId, int cardId) {
        String sql = "SELECT quantity FROM deck_cards WHERE deckid = ? AND cardid = ?";

        List<Integer> results = jdbcTemplate.query(sql, new Object[]{deckId, cardId}, (rs, rowNum) ->
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
}