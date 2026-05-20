package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TradeLogRepository implements ITradeLogRepository {
    private final JdbcTemplate jdbcTemplate;

    public TradeLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertLog(User buyer, User seller, Card card) {
        String sql = "INSERT INTO trade_log (buyer_id, seller_id, card_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, buyer.getUserId(), seller.getUserId(), card.getCardId());
    }
}
