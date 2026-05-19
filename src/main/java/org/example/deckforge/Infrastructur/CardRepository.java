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
        String sql = "INSERT INTO cards (cardName, typeId, rarity) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, card.getCardName(), card.getCardType(), card.getCardRarity());
    }

    @Override
    public Card findById(int cardId) {
        String sql = "SELECT * FROM cards WHERE cardId = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{cardId}, (rs, rowNum) ->
                new Card(
                        rs.getInt("cardId"),
                        rs.getString("cardName"),
                        rs.getString("image"),
                        rs.getString("rarity")
                )
        );
    }

    @Override
    public List<Card> findAllCards() {
        String sql = "SELECT cardId, cardName, typeId, rarity FROM cards";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Card(
                        rs.getInt("cardId"),
                        rs.getString("cardName"),
                        rs.getString("typeid"),
                        rs.getString("rarity")
                )
        );
    }
}
