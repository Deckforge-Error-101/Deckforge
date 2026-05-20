package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.Deck;
import org.example.deckforge.Domain.User;
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
        String sql = "INSERT INTO decks (deckName, formatType, slots, userId) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                deck.getDeckName(),
                deck.getFormatType(),
                deck.getSlots(),
                deck.getUserId()
        );
    }

    @Override
    public List<Deck> findAllDecksByUserId(User user) {
        String sql = "SELECT deckId, deckName, formatType, slots, is_public, userId FROM decks WHERE userId = ?";

        return jdbcTemplate.query(sql, new Object[]{user.getUserId()}, (rs, rowNum) ->
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
        String sql = "UPDATE decks SET deckName = ?, formatType = ?, slots = ?, is_public = ? WHERE deckId = ?";

        jdbcTemplate.update(sql,
                deck.getDeckName(),
                deck.getFormatType(),
                deck.getSlots(),
                deck.isPublic(),
                deck.getDeckId()
        );
    }

    @Override
    public void deleteDeck(Deck deck) {
        String sql = "DELETE FROM decks WHERE deckId = ?";

        jdbcTemplate.update(sql, deck.getDeckId());
    }


    @Override
    public Deck findDeckById(Deck deck) {
        String sql = "SELECT deckId, deckName, formatType, slots, is_public, userId FROM decks WHERE deckId = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Deck(
                        rs.getInt("deckId"),
                        rs.getString("deckName"),
                        rs.getString("formatType"),
                        rs.getInt("slots"),
                        rs.getBoolean("is_public"),
                        rs.getInt("userId")
                ), deck.getDeckId()
        );
    }

    @Override
    public List<Card> getCardsInDeck(Deck deck) {
        //Vi joiner 'cards' (c) med 'deck_cards' (dc) for at koble kort-data sammen med dæk-ID og antal
        String sql = "SELECT c.cardId, c.cardName, c.typeId, c.rarity, dc.quantity " +
                "FROM cards c " +
                "JOIN deck_cards dc ON c.cardId = dc.cardId " +
                "WHERE dc.deckId = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Card(
                        rs.getInt("cardId"),
                        rs.getString("cardName"),
                        rs.getString("typeId"),
                        rs.getString("rarity"),
                        rs.getInt("quantity"),
                        rs.getString("setType")
                ), deck.getDeckId()
        );
    }

    @Override
    public void addCardToDeck(Deck deck, Card card) {
        //Duplicate key så vi ikke behøver flere rækker med samme kort.
        String sql = "INSERT INTO Deck_Cards (deckId, cardId, quantity) VALUES (?, ?, 1) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + 1";
        jdbcTemplate.update(sql, deck.getDeckId(), card.getCardId());
    }

    public void removeCardFromDeck(Deck deck, Card card) {
        //Vi starter med at tjekke hvor mange af det specifikke kort der er i quantity.
        String checkSql = "SELECT quantity FROM deck_cards WHERE deckId = ? AND cardId = ?";

        try {
            //Her forventer vi at modtage en integer tilbage. 1,2,3,4,5 osv
            Integer currentQuantity = jdbcTemplate.queryForObject(checkSql, Integer.class, deck.getDeckId(), card.getCardId());
            //Hvis resultatet er mindre større end 1 fjerne vi bare 1 fra quantity
            if (currentQuantity != null && currentQuantity > 1) {
                jdbcTemplate.update("UPDATE deck_cards SET quantity = quantity - 1 WHERE deckId = ? AND cardId = ?", deck.getDeckId(), card.getCardId());
            } else {
                //Hvis resultatet er 1 fjerner vi hele rækken fra databasen.
                jdbcTemplate.update("DELETE FROM deck_cards WHERE deckId = ? AND cardId = ?", deck.getDeckId(), card.getCardId());
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void updateDeckVisibility(Deck deck) {
        String sql = "UPDATE decks SET is_public = ? WHERE deckId = ?";
        jdbcTemplate.update(sql, deck.isPublic(), deck.getDeckId());
    }

    @Override
    public List<Deck> findAllPublicDecks() {
        String sql = "SELECT * FROM decks WHERE is_public = 1";

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

    public int getQuantityInDeck(Deck deck, Card card) {
        String sql = "SELECT quantity FROM deck_cards WHERE deckid = ? AND cardid = ?";

        List<Integer> results = jdbcTemplate.query(sql, new Object[]{deck.getDeckId(), card.getCardId()}, (rs, rowNum) ->
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