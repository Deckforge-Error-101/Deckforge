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

    }

    @Override
    public List<Deck> findAllDecksByUserId(int userId) {
        return List.of();
    }

    @Override
    public void updateDeck(Deck deck) {

    }

    @Override
    public void deleteDeck(Deck deck) {

    }
}
