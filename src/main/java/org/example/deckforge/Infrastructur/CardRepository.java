package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CardRepository implements ICardRepository {
    private final JdbcTemplate jdbcTemplate;

    public CardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createCard(Card card) {

    }

    @Override
    public Card findById(int cardId) {
        return null;
    }

    @Override
    public List<Card> findAllCards() {
        return List.of();
    }
}
