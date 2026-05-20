package org.example.deckforge.Infrastructur;

import org.example.deckforge.Domain.Card;
import org.example.deckforge.Domain.User;

public interface ITradeLogRepository {
    void insertLog(User buyer, User seller, Card card);
}
