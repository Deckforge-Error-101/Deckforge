package org.example.deckforge.Infrastructur;

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
        String sql = "SELECT deckId, deckname, formatType, slots, userId FROM decks WHERE userId = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) ->
                new Deck(
                        rs.getInt("deckId"),
                        rs.getString("deckname"),
                        rs.getString("formatType"),
                        rs.getInt("slots"),
                        rs.getInt("userId")
                )
        );
    }

    @Override
    public void updateDeck(Deck deck) {
        String sql = "UPDATE decks SET deckname = ?, formatType = ?, slots = ? WHERE deckId = ?";

        jdbcTemplate.update(sql,
                deck.getDeckName(),
                deck.getFormatType(),
                deck.getSlots(),
                deck.getDeckId()
        );
    }

    @Override
    public void deleteDeck(Deck deck) {
        String sql = "DELETE FROM decks WHERE deckId = ?";

        jdbcTemplate.update(sql, deck.getDeckId());
    }
}